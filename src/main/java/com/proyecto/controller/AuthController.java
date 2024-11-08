package com.proyecto.controller;

import com.proyecto.client.AuthClient;
import com.proyecto.dto.AuthDTO;
import com.proyecto.dto.LoginDTO;
import com.proyecto.dto.RegisterDTO;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthClient authClient;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }
    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginDTO loginDTO,
                               HttpSession session, Model model) {
        try {
            AuthDTO response = authClient.login(loginDTO);

            // Guardamos el token
            session.setAttribute("JWT_TOKEN", response.getToken());
            session.setAttribute("USERNAME", response.getUsername());

            // Aseguramos que los roles están en el formato correcto (con el prefijo ROLE_)
            Set<String> formattedRoles = response.getRoles().stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .collect(Collectors.toSet());

            session.setAttribute("USER_ROLES", formattedRoles);

            logger.debug("Roles guardados en sesión: {}", formattedRoles);

            return "redirect:/home";

        } catch (FeignException e) {
            logger.error("Error en login: ", e);
            model.addAttribute("error", "Credenciales inválidas");
            return "/login";
        }
    }


    @GetMapping("/home")
    public String dashboard(HttpSession session, Model model) {

        String username = (String) session.getAttribute("USERNAME");
        model.addAttribute("username", username);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
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
            authClient.register(registerDTO);
            redirectAttributes.addFlashAttribute("success", "Registro exitoso. Por favor inicia sesión.");
            return "redirect:/login";
        } catch (FeignException e) {
            redirectAttributes.addFlashAttribute("error", "Error en el registro: " + e.getMessage());
            return "redirect:/register";
        }
    }

}