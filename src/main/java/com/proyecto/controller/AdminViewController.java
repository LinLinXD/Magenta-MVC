package com.proyecto.controller;

import com.proyecto.aspects.RequireRole;
import com.proyecto.client.AppointmentClient;
import com.proyecto.dto.AppointmentDTO;
import com.proyecto.dto.AppointmentStatus;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminViewController {

    private static final Logger log = LoggerFactory.getLogger(AdminViewController.class);
    private final AppointmentClient appointmentClient;

    @RequireRole("ROLE_ADMIN")
    @GetMapping("/admin")
    public String admin(
            HttpSession session,
            Model model,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("USERNAME");
        Set<String> roles = (Set<String>) session.getAttribute("USER_ROLES");

        try {
            // Obtener todas las citas
            List<AppointmentDTO> appointments = appointmentClient.getAllAppointments();
            log.debug("Citas obtenidas: {}", appointments.size());

            // Aplicar filtros si existen
            if (status != null && !status.isEmpty()) {
                appointments = appointments.stream()
                        .filter(a -> a.getStatus().toString().equals(status))
                        .collect(Collectors.toList());
                log.debug("Filtrado por estado {}: {} citas", status, appointments.size());
            }

            if (eventType != null && !eventType.isEmpty()) {
                appointments = appointments.stream()
                        .filter(a -> a.getEventType().toString().equals(eventType))
                        .collect(Collectors.toList());
                log.debug("Filtrado por tipo de evento {}: {} citas", eventType, appointments.size());
            }

            if (date != null) {
                appointments = appointments.stream()
                        .filter(a -> a.getAppointmentDateTime().toLocalDate().equals(date))
                        .collect(Collectors.toList());
                log.debug("Filtrado por fecha {}: {} citas", date, appointments.size());
            }

            model.addAttribute("username", username);
            model.addAttribute("roles", roles);
            model.addAttribute("appointments", appointments);

            return "admin/admin";
        } catch (Exception e) {
            log.error("Error al cargar el panel de administración", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al cargar el panel de administración: " + e.getMessage());
            return "redirect:/home";
        }
    }

    @RequireRole("ROLE_ADMIN")
    @PostMapping("/admin/appointments/{id}/status")
    public String updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            log.debug("Actualizando estado de cita {}: {}", id, status);
            appointmentClient.updateAppointmentStatus(id, status);
            redirectAttributes.addFlashAttribute("success",
                    "Estado de la cita actualizado correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar estado de cita", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar el estado de la cita: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @RequireRole("ROLE_ADMIN")
    @GetMapping("/admin/appointments/{id}/responses")
    public String viewAppointmentResponses(
            @PathVariable Long id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("USERNAME");
        @SuppressWarnings("unchecked")
        Set<String> roles = (Set<String>) session.getAttribute("USER_ROLES");

        // Verificar si el usuario tiene el rol ROLE_ADMIN
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para ver las respuestas");
            return "redirect:/home";
        }

        try {
            log.debug("Obteniendo respuestas para la cita {}", id);
            AppointmentDTO appointment = appointmentClient.getAppointment(id);

            // Obtener todas las citas para mantener la vista principal actualizada
            List<AppointmentDTO> appointments = appointmentClient.getAllAppointments();

            model.addAttribute("username", username);
            model.addAttribute("roles", roles);
            model.addAttribute("appointments", appointments);
            model.addAttribute("selectedAppointment", appointment);

            return "admin/admin";
        } catch (Exception e) {
            log.error("Error al cargar respuestas de la cita {}", id, e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al cargar las respuestas: " + e.getMessage());
            return "redirect:/admin";
        }
    }
}