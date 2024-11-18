package com.proyecto.client;

import com.proyecto.configuration.FeignConfig;
import com.proyecto.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name = "appointment-service",
        url = "${auth.service.url:localhost:8080}",
        configuration = FeignConfig.class
)
public interface AppointmentClient {
    @PostMapping("/appointments")
    AppointmentDTO createAppointment(@RequestBody CreateAppointmentDTO createDTO,
                                     @RequestParam String username);

    @GetMapping("/appointments/user/{username}")
    List<AppointmentDTO> getUserAppointments(@PathVariable String username);

    @GetMapping("/appointments/{appointmentId}")
    AppointmentDTO getAppointment(@PathVariable Long appointmentId);

    @PutMapping("/appointments/{appointmentId}/status")
    AppointmentDTO updateAppointmentStatus(@PathVariable Long appointmentId,
                                           @RequestParam AppointmentStatus status);

    @DeleteMapping("/appointments/{appointmentId}")
    void cancelAppointment(@PathVariable Long appointmentId);

    @GetMapping("/appointments/availability")
    List<TimeSlotDTO> getAvailability(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @GetMapping("/questionnaire/questions/{eventType}")
    List<QuestionnaireQuestionDTO> getQuestionsByEventType(@PathVariable EventType eventType);

    @GetMapping("/admin/appointments")
    List<AppointmentDTO> getAllAppointments();
}