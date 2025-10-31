package com.promhil.spring_spring_promhil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.promhil.spring_spring_promhil.dto.AutenticacionFilter;
import com.promhil.spring_spring_promhil.model.Usuario;
import com.promhil.spring_spring_promhil.service.AutenticacionService;
import com.promhil.spring_spring_promhil.util.Alert;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AutenticacionService autenticacionService;

    @GetMapping({"/", "login"})
    public String login(Model model) {
        model.addAttribute("filter", new AutenticacionFilter());
        return "login";
    }

    @PostMapping("iniciar-sesion")
    public String iniciarSesion(@ModelAttribute AutenticacionFilter filter,
                                Model model,
                                RedirectAttributes flash,
                                HttpSession session) {

        Usuario usuario = autenticacionService.autenticar(filter);

        if (usuario == null) {
            model.addAttribute("alert", Alert.sweetAlertError("Credenciales incorrectas"));
            model.addAttribute("filter", new AutenticacionFilter());
            return "login";
        }

        if (!usuario.getActivo()) {
            model.addAttribute("alert", Alert.sweetAlertInfo("El usuario se encuentra inactivo"));
            model.addAttribute("filter", new AutenticacionFilter());
            return "login";
        }

        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("nombreCompleto", usuario.getNombres() + " " + usuario.getApellidos());
        session.setAttribute("idTipo", usuario.getTipo().getIdTipo());

        flash.addFlashAttribute("alert",
                Alert.sweetImageUrl(
                        "Bienvenido al Sistema de Inventario Eléctrico",
                        usuario.getNombres() + " " + usuario.getApellidos(),
                        "/assets/img/portal_players.gif"
                )
        );

        return "redirect:/dashboard";
    }

    @GetMapping("dashboard")
    public String home() {
        return "dashboard";
    }

    @GetMapping("cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
