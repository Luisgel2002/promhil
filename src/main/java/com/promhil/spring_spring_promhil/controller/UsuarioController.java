package com.promhil.spring_spring_promhil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.promhil.spring_spring_promhil.model.Usuario;
import com.promhil.spring_spring_promhil.service.UsuarioService;
import com.promhil.spring_spring_promhil.util.Alert;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listado(Model model, HttpSession session) {
        Integer idTipo = (Integer) session.getAttribute("idTipo");
        if (idTipo == null || idTipo != 1) {
            return "redirect:/dashboard";
        }

        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "usuario/listado";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpSession session) {
        Integer idTipo = (Integer) session.getAttribute("idTipo");
        if (idTipo == null || idTipo != 1) {
            return "redirect:/dashboard";
        }

        Usuario usuario = usuarioService.getOne(id);
        model.addAttribute("usuario", usuario);
        return "usuario/editar";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario,
                          @RequestParam Integer idTipo,
                          HttpSession session,
                          RedirectAttributes flash) {

        Integer rolSesion = (Integer) session.getAttribute("idTipo");
        if (rolSesion == null || rolSesion != 1) {
            return "redirect:/dashboard";
        }

        usuario.setTipo(usuarioService.construirTipo(idTipo));

        usuarioService.guardar(usuario);

        flash.addFlashAttribute("toast",
                Alert.sweetToast("Usuario guardado correctamente", "success", 4000));

        return "redirect:/usuarios";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Integer id,
                                HttpSession session,
                                RedirectAttributes flash) {
        Integer idTipo = (Integer) session.getAttribute("idTipo");
        if (idTipo == null || idTipo != 1) {
            return "redirect:/dashboard";
        }
        usuarioService.cambiarEstado(id);

        flash.addFlashAttribute("toast",
                Alert.sweetToast("Estado actualizado", "success", 3000));

        return "redirect:/usuarios";
    }
}
