// CustomErrorController.java
package com.proyecto.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    private MessageSource messageSource;

    /**
     * Maneja las solicitudes de error y devuelve la vista de error.
     *
     * @param request la solicitud HTTP.
     * @param model el modelo para la vista.
     * @return el nombre de la vista de error.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Obtener el código de error
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = status != null ? Integer.parseInt(status.toString()) : 500;

        // Obtener el mensaje de error y detalles
        String errorMessage = getErrorMessage(statusCode);
        String errorDetails = getErrorDetails(statusCode);

        // Obtener información adicional del error
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String exceptionMessage = throwable != null ? throwable.getMessage() : null;

        // Añadir atributos al modelo
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDetails", errorDetails);
        model.addAttribute("path", path);
        model.addAttribute("exceptionMessage", exceptionMessage);

        return "error";
    }

    /**
     * Obtiene el mensaje de error basado en el código de estado.
     *
     * @param statusCode el código de estado HTTP.
     * @return el mensaje de error correspondiente.
     */
    private String getErrorMessage(int statusCode) {
        String messageKey;
        switch (statusCode) {
            case 404:
                messageKey = "error.notFound";
                break;
            case 403:
                messageKey = "error.forbidden";
                break;
            case 500:
                messageKey = "error.internal";
                break;
            default:
                messageKey = "error.unexpected";
        }
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }

    /**
     * Obtiene los detalles del error basado en el código de estado.
     *
     * @param statusCode el código de estado HTTP.
     * @return los detalles del error correspondientes.
     */
    private String getErrorDetails(int statusCode) {
        String messageKey;
        switch (statusCode) {
            case 404:
                messageKey = "error.notFound.details";
                break;
            case 403:
                messageKey = "error.forbidden.details";
                break;
            case 500:
                messageKey = "error.internal.details";
                break;
            default:
                messageKey = "error.unexpected.details";
        }
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }
}