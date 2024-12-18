package com.proyecto.controller;

import com.proyecto.client.AppointmentClient;
import com.proyecto.client.NotificationClient;
import com.proyecto.dto.*;
import com.proyecto.util.EventDescriptionUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentViewController {
    private final AppointmentClient appointmentClient;
    private final NotificationClient notificationClient;

    /**
     * Añade atributos de notificación al modelo.
     *
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     */
    @ModelAttribute
    public void addNotificationAttributes(Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        if (username != null) {
            try {
                List<AppointmentNotificationDTO> notifications = notificationClient.getUnreadNotifications(username);
                if (notifications != null) {
                    // Contar solo las notificaciones enviadas y no leídas
                    long unreadCount = notifications.stream()
                            .filter(n -> n.isSent() && !n.isRead())
                            .count();

                    model.addAttribute("notifications", notifications);
                    model.addAttribute("unreadNotificationsCount", unreadCount);
                } else {
                    model.addAttribute("notifications", new ArrayList<>());
                    model.addAttribute("unreadNotificationsCount", 0);
                }
            } catch (Exception e) {
                model.addAttribute("notifications", new ArrayList<>());
                model.addAttribute("unreadNotificationsCount", 0);
            }
        }
    }

    /**
     * Muestra la página principal de citas.
     *
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     * @return la vista de la página principal de citas.
     */
    @GetMapping
    public String showAppointmentsIndex(Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("username", username);
            return "appointments/index";
        } catch (Exception e) {
            log.error("Error al cargar la página principal: ", e);
            return "home";
        }
    }

    /**
     * Muestra el calendario de disponibilidad de citas.
     *
     * @param eventType el tipo de evento para filtrar la disponibilidad.
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     * @return la vista del calendario de disponibilidad.
     */
    @GetMapping("/calendar")
    public String showCalendar(
            @RequestParam(required = true) EventType eventType,
            Model model,
            HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");

        if(username == null){
            return "redirect:/login";
        }


        try {
            model.addAttribute("username", username);

            YearMonth currentMonth = YearMonth.now();
            LocalDate firstDay = currentMonth.atDay(1);
            LocalDate lastDay = currentMonth.atEndOfMonth();

            List<LocalDate> daysInMonth = new ArrayList<>();

            DayOfWeek firstDayOfWeek = firstDay.getDayOfWeek();
            int paddingDays = firstDayOfWeek.getValue() - 1;

            for (int i = 0; i < paddingDays; i++) {
                daysInMonth.add(null);
            }

            for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
                daysInMonth.add(date);
            }

            List<TimeSlotDTO> availability = new ArrayList<>();
            for (LocalDate date : daysInMonth) {
                if (date != null) {
                    List<TimeSlotDTO> daySlots = appointmentClient.getAvailability(date);
                    if (!daySlots.isEmpty()) {
                        availability.add(daySlots.get(0)); // Tomamos el primer slot como representativo del día
                    }
                } else {
                    // Agregar un slot vacío para los días de padding
                    availability.add(TimeSlotDTO.builder()
                            .available(false)
                            .build());
                }
            }

            model.addAttribute("availability", availability);
            model.addAttribute("daysInMonth", daysInMonth);
            model.addAttribute("currentMonth", currentMonth);
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("eventType", eventType);

            return "appointments/calendar";
        } catch (Exception e) {
            log.error("Error al obtener disponibilidad: ", e);
            model.addAttribute("error", "Error al cargar la disponibilidad");
            return "appointments/calendar";
        }
    }

    /**
     * Muestra el formulario para agendar una nueva cita.
     *
     * @param eventType el tipo de evento para la cita.
     * @param date la fecha de la cita.
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     * @return la vista del formulario de cita.
     */
    @GetMapping("/new")
    public String showAppointmentForm(
            @RequestParam(required = false) EventType eventType,
            @RequestParam(required = false) LocalDate date,
            Model model,
            HttpSession session
    ) {
        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            return "redirect:/login";
        }

        // Si no hay tipo de evento seleccionado, mostrar página de selección
        if (eventType == null) {
            model.addAttribute("username", username); // Agregar username aquí también
            model.addAttribute("eventTypes", EventType.values());
            model.addAttribute("eventDescriptionUtil", EventDescriptionUtil.class);
            return "appointments/select-event-type";
        }

        try {


            // Si hay tipo de evento pero no fecha, mostrar calendario
            if (date == null) {
                return "redirect:/appointments/calendar?eventType=" + eventType;
            }

            // Obtener slots de tiempo disponibles para la fecha
            List<TimeSlotDTO> timeSlots = appointmentClient.getAvailability(date);

            // Obtener preguntas específicas para el tipo de evento
            List<QuestionnaireQuestionDTO> questions =
                    appointmentClient.getQuestionsByEventType(eventType);

            model.addAttribute("username", username); // Usar model.addAttribute en lugar de redirectAttributes
            model.addAttribute("eventType", eventType);
            model.addAttribute("date", date);
            model.addAttribute("timeSlots", timeSlots);
            model.addAttribute("questions", questions);
            model.addAttribute("createAppointmentDTO", new CreateAppointmentDTO());

            return "appointments/form";
        } catch (Exception e) {
            log.error("Error al cargar el formulario: ", e);
            model.addAttribute("error", "Error al cargar el formulario");
            return "redirect:/appointments";
        }
    }

    /**
     * Crea una nueva cita.
     *
     * @param createDTO el DTO con los datos de la cita.
     * @param bindingResult el resultado de la validación del formulario.
     * @param session la sesión HTTP actual.
     * @param redirectAttributes atributos para redirección.
     * @return la redirección a la vista de mis citas.
     */
    @PostMapping("/new")
    public String createAppointment(
            @ModelAttribute CreateAppointmentDTO createDTO,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            return "redirect:/login";
        }

        if (createDTO.getAppointmentDateTime() == null) {
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar una fecha y hora para la cita");
            return "redirect:/appointments/new?eventType=" + createDTO.getEventType();
        }

        if (createDTO.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "No se puede agendar una cita en el pasado");
            return "redirect:/appointments/new?eventType=" + createDTO.getEventType();
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Por favor complete todos los campos requeridos");
            return "redirect:/appointments/new?eventType=" + createDTO.getEventType();
        }

        try {


            AppointmentDTO appointment = appointmentClient.createAppointment(createDTO, username);
            redirectAttributes.addFlashAttribute("success",
                    "Cita agendada exitosamente para el " +
                            appointment.getAppointmentDateTime().format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm")
                            )
            );
            return "redirect:/appointments/my-appointments";
        } catch (FeignException e) {
            String errorMessage = e.contentUTF8();
            if (errorMessage.contains("error\":\"")) {
                errorMessage = errorMessage.split("error\":\"")[1].split("\"")[0];
            }
            redirectAttributes.addFlashAttribute("error", "Error al agendar la cita: " + errorMessage);
            return "redirect:/appointments/new?eventType=" + createDTO.getEventType();
        }
    }

    /**
     * Muestra las citas del usuario actual.
     *
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     * @return la vista de mis citas.
     */
    @GetMapping("/my-appointments")
    public String showMyAppointments(Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");

        if (username == null) {
            return "redirect:/login";
        }

        try {
            model.addAttribute("username", username);
            List<AppointmentDTO> appointments = appointmentClient.getUserAppointments(username);
            model.addAttribute("appointments", appointments);
            return "appointments/my-appointments";
        } catch (Exception e) {
            log.error("Error al obtener las citas: ", e);
            model.addAttribute("error", "Error al cargar las citas");
            return "appointments/my-appointments";
        }
    }

    /**
     * Cancela una cita.
     *
     * @param id el ID de la cita a cancelar.
     * @param session la sesión HTTP actual.
     * @param redirectAttributes atributos para redirección.
     * @return la redirección a la vista de mis citas.
     */
    @PostMapping("/{id}/cancel")
    public String cancelAppointment(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            return "redirect:/login";
        }

        try {
            appointmentClient.cancelAppointment(id);
            redirectAttributes.addFlashAttribute("success", "Cita cancelada exitosamente");
        } catch (Exception e) {
            log.error("Error al cancelar la cita: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la cita");
        }

        return "redirect:/appointments/my-appointments";
    }

    /**
     * Muestra el detalle de una cita.
     *
     * @param appointmentId el ID de la cita.
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     * @return la vista del detalle de la cita.
     */
    @GetMapping("/{appointmentId}")
    public String showAppointmentDetail(@PathVariable Long appointmentId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            return "redirect:/login";
        }

        try {
            AppointmentDTO appointment = appointmentClient.getAppointment(appointmentId);
            model.addAttribute("appointment", appointment);
            model.addAttribute("username", username);
            return "appointments/detail";
        } catch (Exception e) {
            log.error("Error al obtener la cita: ", e);
            model.addAttribute("error", "No se pudo cargar la información de la cita");
            return "redirect:/appointments/my-appointments";
        }
    }
}