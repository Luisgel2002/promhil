package com.promhil.spring_spring_promhil.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.promhil.spring_spring_promhil.dto.ProductoSeleccionado;
import com.promhil.spring_spring_promhil.dto.ResultadoResponse;
import com.promhil.spring_spring_promhil.model.Boleta;
import com.promhil.spring_spring_promhil.model.DetalleBoleta;
import com.promhil.spring_spring_promhil.model.Producto;
import com.promhil.spring_spring_promhil.model.Usuario;
import com.promhil.spring_spring_promhil.service.BoletaService;
import com.promhil.spring_spring_promhil.service.ProductoService;
import com.promhil.spring_spring_promhil.service.UsuarioService;
import com.promhil.spring_spring_promhil.util.Alert;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("boleta")
@SessionAttributes("lstSeleccionado")
public class BoletaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BoletaService boletaService;

    @ModelAttribute("lstSeleccionado")
    public List<ProductoSeleccionado> inicializarSeleccionados() {
        return new ArrayList<>();
    }

    @GetMapping("listado")
    public String listado(Model model) {
        model.addAttribute("lstBoleta", boletaService.getAll());
        return "boleta/listado";
    }

    @GetMapping("filtrar")
    public String filtrar(Model model) {
        return "boleta/listado";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model) {
        model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
        model.addAttribute("productos", productoService.getAll());
        return "boleta/nuevo";
    }

    @PostMapping("agregar")
    public String agregar(@ModelAttribute ProductoSeleccionado seleccionado,
                          @ModelAttribute("lstSeleccionado") List<ProductoSeleccionado> lstSeleccionado,
                          Model model) {

        model.addAttribute("productos", productoService.getAll());

        boolean existeProducto = lstSeleccionado.stream()
                .anyMatch(item -> item.getIdProducto() == seleccionado.getIdProducto());

        if (existeProducto) {
            model.addAttribute("alert", Alert.sweetAlertInfo("El producto ya fue seleccionado"));
            return "boleta/nuevo";
        }

        Producto producto = productoService.getOne(seleccionado.getIdProducto());
        if (seleccionado.getCantidad() > producto.getStock()) {
            model.addAttribute("alert", Alert.sweetAlertInfo("El stock es insuficiente"));
            return "boleta/nuevo";
        }

        lstSeleccionado.add(seleccionado);
        model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
        return "boleta/nuevo";
    }

    @PostMapping("quitar")
    public String quitar(@RequestParam Integer idProducto,
                         @ModelAttribute("lstSeleccionado") List<ProductoSeleccionado> lstSeleccionado,
                         Model model) {

        lstSeleccionado.removeIf(item -> item.getIdProducto() == idProducto);

        model.addAttribute("productos", productoService.getAll());
        model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
        return "boleta/nuevo";
    }

    @PostMapping("registrar")
    public String registrar(@ModelAttribute("lstSeleccionado") List<ProductoSeleccionado> lstSeleccionado,
                            Model model,
                            RedirectAttributes flash,
                            HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Sesión expirada"));
            return "redirect:/login";
        }

        if (lstSeleccionado == null || lstSeleccionado.isEmpty()) {
            model.addAttribute("alert", Alert.sweetAlertInfo("Agregue por lo menos 1 producto"));
            model.addAttribute("productos", productoService.getAll());
            model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
            return "boleta/nuevo";
        }

        Boleta boleta = new Boleta();
        Usuario usuario = usuarioService.getOne(idUsuario);
        boleta.setUsuario(usuario);

        List<DetalleBoleta> lstDetalleBoleta = lstSeleccionado.stream().map(item -> {
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setIdProducto(item.getIdProducto());

            Producto producto = productoService.getOne(item.getIdProducto());
            detalle.setProducto(producto);

            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioVenta(item.getPrecio());

            return detalle;
        }).toList();

        boleta.setLstDetalleBoleta(lstDetalleBoleta);

        ResultadoResponse response = boletaService.create(boleta);

        if (!response.success) {
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje));
            model.addAttribute("productos", productoService.getAll());
            model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
            return "boleta/nuevo";
        }

        flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje, "success", 5000));
        session.removeAttribute("lstSeleccionado");
        return "redirect:/boleta/listado";
    }

}
