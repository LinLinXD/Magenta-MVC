package com.proyecto.controller;

import com.proyecto.client.AuthClient;
import com.proyecto.client.NotificationClient;
import com.proyecto.dto.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
     * Muestra la página de inicio de sesión.
     *
     * @param model el modelo para la vista.
     * @return la vista de la página de inicio de sesión.
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    /**
     * Maneja la solicitud de cierre de sesión.
     *
     * @param session la sesión HTTP actual.
     * @return la redirección a la página de inicio con el parámetro de cierre de sesión.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home?logout=true";
    }

    /**
     * Muestra la página de inicio.
     *
     * @param session la sesión HTTP actual.
     * @param model el modelo para la vista.
     * @return la vista de la página de inicio.
     */
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String username = (String) session.getAttribute("USERNAME");
        Set<String> roles = (Set<String>) session.getAttribute("USER_ROLES");
        if (username != null) {
            try {
                AuthDTO userInfo = authClient.getUserInfo(username);

                // Agregar información básica del usuario
                model.addAttribute("roles", roles);
                model.addAttribute("username", username);

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
                model.addAttribute("profileImageUrl", "/images/default-profile-picture.png");
            }
        }

        return "/home";
    }

    /**
     * Procesa la solicitud de inicio de sesión.
     *
     * @param loginDTO el DTO con los datos de inicio de sesión.
     * @param session la sesión HTTP actual.
     * @param model el modelo para la vista.
     * @return la redirección a la página de inicio.
     */
    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginDTO loginDTO,
                               HttpSession session,
                               Model model) {
        try {
            AuthDTO response = authClient.login(loginDTO);

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

    /**
     * Muestra la página de registro.
     *
     * @param model el modelo para la vista.
     * @return la vista de la página de registro.
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    /**
     * Procesa la solicitud de registro.
     *
     * @param registerDTO el DTO con los datos de registro.
     * @param redirectAttributes atributos para redirección.
     * @return la redirección a la página de inicio de sesión.
     */
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

    /**
     * Procesa la solicitud de modificación de usuario.
     *
     * @param modifyUserDTO el DTO con los datos de modificación de usuario.
     * @param session la sesión HTTP actual.
     * @param redirectAttributes atributos para redirección.
     * @return la redirección a la página de modificación de usuario.
     */
    @PostMapping("/modifyUser")
    public String modifyUser(@ModelAttribute ModifyUserDTO modifyUserDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            return "redirect:/login";
        }

        try {
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
                }
                redirectAttributes.addFlashAttribute("successMessage",
                        "Perfil actualizado correctamente");
            }

            return "redirect:/modifyUser";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar el perfil: " + e.getMessage());
            return "redirect:/modifyUser";
        }
    }

    /**
     * Muestra el formulario de modificación de usuario.
     *
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     * @return la vista del formulario de modificación de usuario.
     */
    @GetMapping("/modifyUser")
    public String showModifyUserForm(Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");

        if (username == null) {
            return "redirect:/login";
        } else {
            String profileImageUrl = (String) session.getAttribute("PROFILE_IMAGE_URL");
            ModifyUserDTO modifyUserDTO = new ModifyUserDTO();
            model.addAttribute("modifyUserDTO", modifyUserDTO);
            model.addAttribute("currentProfileImage", profileImageUrl);
            return "modifyUser";
        }
    }

    /**
     * Refresca las notificaciones del usuario.
     *
     * @param session la sesión HTTP actual.
     * @return un mapa con las notificaciones y el estado de la operación.
     */
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
}