package com.promhil.spring_spring_promhil.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalAttributes {

    @ModelAttribute
    public void setGlobalAttributes(Model model, HttpSession session) {
        Object nombre = session.getAttribute("nombreCompleto");
        if (nombre != null) {
            model.addAttribute("nombreCompleto", nombre);
        }
        
        Object idUsuario = session.getAttribute("idUsuario");
        if (idUsuario != null) {
            model.addAttribute("idUsuario", idUsuario);
        }
        
        Object idTipo = session.getAttribute("idTipo");
        if (idTipo != null) {
            model.addAttribute("idTipo", idTipo);
        }
    }
}
