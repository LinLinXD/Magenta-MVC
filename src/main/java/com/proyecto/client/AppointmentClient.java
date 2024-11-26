package com.proyecto.client;

import com.proyecto.configuration.FeignConfig;
import com.proyecto.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Un cliente Feign para el servicio de citas.
 */
@FeignClient(
        name = "appointment-service",
        url = "${auth.service.url:localhost:8080}",
        configuration = FeignConfig.class
)
public interface AppointmentClient {

    /**
     * Crea una nueva cita.
     *
     * @param createDTO el DTO de creación de la cita.
     * @param username el nombre de usuario del usuario que crea la cita.
     * @return el DTO de la cita creada.
     */
    @PostMapping("/appointments")
    AppointmentDTO createAppointment(@RequestBody CreateAppointmentDTO createDTO,
                                     @RequestParam String username);

    /**
     * Obtiene las citas de un usuario.
     *
     * @param username el nombre de usuario del usuario para el que se obtienen las citas.
     * @return una lista de citas del usuario.
     */
    @GetMapping("/appointments/user/{username}")
    List<AppointmentDTO> getUserAppointments(@PathVariable String username);

    /**
     * Obtiene una cita específica.
     *
     * @param appointmentId el ID de la cita.
     * @return la cita con el ID especificado.
     */
    @GetMapping("/appointments/{appointmentId}")
    AppointmentDTO getAppointment(@PathVariable Long appointmentId);

    /**
     * Actualiza el estado de una cita específica.
     *
     * @param appointmentId el ID de la cita.
     * @param status el nuevo estado de la cita.
     * @return la cita actualizada.
     */
    @PutMapping("/appointments/{appointmentId}/status")
    AppointmentDTO updateAppointmentStatus(@PathVariable Long appointmentId,
                                           @RequestParam AppointmentStatus status);

    /**
     * Cancela una cita específica.
     *
     * @param appointmentId el ID de la cita.
     */
    @DeleteMapping("/appointments/{appointmentId}")
    void cancelAppointment(@PathVariable Long appointmentId);

    /**
     * Obtiene los horarios disponibles para una fecha específica.
     *
     * @param date la fecha para la cual se verifica la disponibilidad.
     * @return una lista de horarios disponibles.
     */
    @GetMapping("/appointments/availability")
    List<TimeSlotDTO> getAvailability(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    /**
     * Obtiene las preguntas del cuestionario para un tipo de evento específico.
     *
     * @param eventType el tipo de evento.
     * @return una lista de preguntas del cuestionario para el tipo de evento.
     */
    @GetMapping("/questionnaire/questions/{eventType}")
    List<QuestionnaireQuestionDTO> getQuestionsByEventType(@PathVariable EventType eventType);

    /**
     * Obtiene todas las citas (acceso de administrador).
     *
     * @return una lista de todas las citas.
     */
    @GetMapping("/admin/appointments")
    List<AppointmentDTO> getAllAppointments();
}