package com.proyecto.controller;

import com.proyecto.client.AuthClient;
import com.proyecto.client.NotificationClient;
import com.proyecto.dto.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthClient authClient;
    private final NotificationClient notificationClient;  // Usar client en lugar de service

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String DEFAULT_PROFILE_IMAGE = "/images/default-profile-picture.png";
    private static final String BACKEND_URL = "http://localhost:8080"; // URL de tu backend

    @ModelAttribute
    public void addNotificationAttributes(Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        if (username != null) {
            try {
                refreshNotifications(model, username);
            } catch (Exception e) {
                model.addAttribute("notifications", List.of());
                model.addAttribute("unreadNotificationsCount", 0);
            }
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home?logout=true";
    }


    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String username = (String) session.getAttribute("USERNAME");
        if (username != null) {
            try {
                AuthDTO userInfo = authClient.getUserInfo(username);

                // Agregar información básica del usuario
                model.addAttribute("username", username);
                model.addAttribute("userRoles", session.getAttribute("USER_ROLES"));

                // Obtener y agregar notificaciones usando el client
                List<AppointmentNotificationDTO> notifications =
                        notificationClient.getUnreadNotifications(username);
                model.addAttribute("notifications", notifications);
                model.addAttribute("unreadNotificationsCount", notificationClient.getUnreadNotificationCount(username));

                // Manejo de la imagen de perfil
                String profileImageUrl = null;
                String sessionImageUrl = (String) session.getAttribute("PROFILE_IMAGE_URL");

                if (sessionImageUrl != null && !sessionImageUrl.isEmpty()) {
                    profileImageUrl = sessionImageUrl;
                } else if (userInfo.getProfileImageUrl() != null && !userInfo.getProfileImageUrl().isEmpty()) {
                    profileImageUrl = userInfo.getProfileImageUrl();
                    session.setAttribute("PROFILE_IMAGE_URL", profileImageUrl);
                }

                if (profileImageUrl == null || profileImageUrl.isEmpty()) {
                    profileImageUrl = "/images/default-profile-picture.png";
                }

                model.addAttribute("profileImageUrl", profileImageUrl);
                model.addAttribute("userInfo", userInfo);
            } catch (Exception e) {
                logger.error("Error al obtener información del usuario: {}", e.getMessage());
                model.addAttribute("profileImageUrl", "/images/default-profile-picture.png");
            }
        }

        return "/home";
    }


    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginDTO loginDTO,
                               HttpSession session,
                               Model model) {
        try {
            AuthDTO response = authClient.login(loginDTO);
            model.addAttribute("RegisterDTO", new RegisterDTO());

            // Guardar el token y la información básica
            session.setAttribute("JWT_TOKEN", response.getToken());
            session.setAttribute("USERNAME", response.getUsername());

            // Guardar la URL de la imagen de perfil si está disponible
            if (response.getProfileImageUrl() != null) {
                session.setAttribute("PROFILE_IMAGE_URL", response.getProfileImageUrl());
            }

            // Manejar los roles de manera segura
            Set<String> formattedRoles = new HashSet<>();
            if (response.getRoles() != null) {
                formattedRoles = response.getRoles().stream()
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .collect(Collectors.toSet());
            } else {
                // Si no hay roles, al menos agregar un rol por defecto
                formattedRoles.add("ROLE_USER");
            }
            session.setAttribute("USER_ROLES", formattedRoles);

            return "redirect:/home";

        } catch (Exception e) {
            model.addAttribute("error", "Credenciales inválidas");
            return "/login";
        }
    }



    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegisterDTO registerDTO,
                                  RedirectAttributes redirectAttributes) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);


            AuthDTO response = authClient.register(registerDTO);

            if (response != null && response.getError() == null) {
                redirectAttributes.addFlashAttribute("success", "Registro exitoso. Por favor inicia sesión.");
                return "redirect:/login";
            } else {
                String error = response != null && response.getError() != null ?
                        response.getError() : "Error desconocido en el registro";
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/register";
            }
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Error en el registro: " + e.getMessage());
            return "redirect:/register";
        }
    }



    @PostMapping("/modifyUser")
    public String modifyUser(@ModelAttribute ModifyUserDTO modifyUserDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            return "redirect:/login";
        }

        try {
            logger.debug("📤 Enviando solicitud de modificación para usuario: {} con imagen: {}",
                    username,
                    modifyUserDTO.getProfileImage() != null ?
                            modifyUserDTO.getProfileImage().getSize() : "null");

            AuthDTO response = authClient.modifyUser(
                    modifyUserDTO.getName(),
                    modifyUserDTO.getEmail(),
                    modifyUserDTO.getPhone(),
                    username,
                    modifyUserDTO.getProfileImage()
            );

            if (response != null) {
                if (response.getProfileImageUrl() != null) {
                    // Guardar la imagen en la sesión
                    session.setAttribute("PROFILE_IMAGE_URL", response.getProfileImageUrl());
                    logger.debug("🖼️ Imagen guardada en sesión: {} caracteres",
                            response.getProfileImageUrl().length());
                }
                redirectAttributes.addFlashAttribute("successMessage",
                        "Perfil actualizado correctamente");
            }

            return "redirect:/modifyUser";
        } catch (Exception e) {
            logger.error("Error al actualizar el perfil: ", e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar el perfil: " + e.getMessage());
            return "redirect:/modifyUser";
        }
    }

    @GetMapping("/modifyUser")
    public String showModifyUserForm(Model model, HttpSession session) {
        // Verificar si hay una imagen en la sesión
        String profileImageUrl = (String) session.getAttribute("PROFILE_IMAGE_URL");
        ModifyUserDTO modifyUserDTO = new ModifyUserDTO();
        model.addAttribute("modifyUserDTO", modifyUserDTO);
        model.addAttribute("currentProfileImage", profileImageUrl);
        return "modifyUser";
    }

    @GetMapping("/refreshNotifications")
    @ResponseBody
    public Map<String, Object> refreshNotifications(HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        Map<String, Object> response = new HashMap<>();

        if (username != null) {
            try {
                var notifications = notificationClient.getUnreadNotifications(username);
                response.put("notifications", notifications);
                response.put("count", notifications.size());
                response.put("success", true);
            } catch (Exception e) {
                response.put("success", false);
                response.put("error", "Error al refrescar notificaciones");
            }
        }

        return response;
    }

    private void refreshNotifications(Model model, String username) {
        var notifications = notificationClient.getUnreadNotifications(username);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadNotificationsCount", notifications.size());

        // También guardamos en sesión para acceso rápido
        model.addAttribute("hasUnreadNotifications", notifications.size() > 0);
    }

}