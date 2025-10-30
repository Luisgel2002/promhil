package com.promhil.spring_spring_promhil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.promhil.spring_spring_promhil.dto.ProductoFilter;
import com.promhil.spring_spring_promhil.dto.ResultadoResponse;
import com.promhil.spring_spring_promhil.model.Producto;
import com.promhil.spring_spring_promhil.service.CategoriaService;
import com.promhil.spring_spring_promhil.service.ProductoService;
import com.promhil.spring_spring_promhil.service.ProveedorService;
import com.promhil.spring_spring_promhil.util.Alert;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private ProveedorService proveedorService;

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstProducto", productoService.getAll());
		model.addAttribute("lstCategoria", categoriaService.getAll());
		model.addAttribute("lstProveedor", proveedorService.getAll());
		model.addAttribute("filter", new ProductoFilter());
		return "producto/listado";
	}

	@GetMapping("filtrar")
	public String filtrar(@ModelAttribute ProductoFilter filter, Model model) {
		model.addAttribute("lstProducto", productoService.search(filter));
		model.addAttribute("lstCategoria", categoriaService.getAll());
		model.addAttribute("lstProveedor", proveedorService.getAll());
		model.addAttribute("filter", filter);
		return "producto/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("lstCategoria", categoriaService.getAll());
		model.addAttribute("lstProveedor", proveedorService.getAll());
		model.addAttribute("producto", new Producto());
		return "producto/nuevo";
	}

	@PostMapping("registrar")
	public String registrar(@ModelAttribute Producto producto, Model model, RedirectAttributes flash) {
		
		producto.setActivo(true);
		var response = productoService.create(producto);

		if (!response.success) {
			model.addAttribute("alert", Alert.sweetAlertError(response.mensaje));
			model.addAttribute("lstCategoria", categoriaService.getAll());
			model.addAttribute("lstProveedor", proveedorService.getAll());
			return "producto/nuevo";
		}

		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje, "success", 5000));
		return "redirect:/producto/listado";
	}

	@GetMapping("edicion/{id}")
	public String edicion(@PathVariable Integer id, Model model) {
		model.addAttribute("lstCategoria", categoriaService.getAll());
		model.addAttribute("lstProveedor", proveedorService.getAll());
		model.addAttribute("producto", productoService.getOne(id));
		return "producto/edicion";
	}

	@PostMapping("guardar")
	public String guardar(@ModelAttribute Producto producto,
	                      Model model,
	                      RedirectAttributes flash,
	                      HttpSession session) {

	    Integer idTipo = (Integer) session.getAttribute("idTipo");

	    Producto actual = productoService.getOne(producto.getIdProducto());

	    if (idTipo == null || idTipo != 1) {
	        producto.setActivo(actual.getActivo());
	    }

	    var response = productoService.update(producto);

	    if (!response.success) {
	        model.addAttribute("alert", Alert.sweetAlertError(response.mensaje));
	        model.addAttribute("lstCategoria", categoriaService.getAll());
	        model.addAttribute("lstProveedor", proveedorService.getAll());
	        return "producto/nuevo";
	    }

	    flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje, "success", 5000));
	    return "redirect:/producto/listado";
	}

	@PostMapping("cambiar-estado")
	public String cambiarEstado(@RequestParam Integer id,
	                            RedirectAttributes flash,
	                            HttpSession session) {

	    Integer idTipo = (Integer) session.getAttribute("idTipo");
	    if (idTipo == null || idTipo != 1) {
	        flash.addFlashAttribute("toast",
	                Alert.sweetToast("No tiene permisos para cambiar estado.", "error", 5000));
	        return "redirect:/producto/listado";
	    }

	    ResultadoResponse response = productoService.cambiarEstado(id);
	    String toast = Alert.sweetToast(response.mensaje, "success", 5000);
	    flash.addFlashAttribute("toast", toast);
	    return "redirect:/producto/listado";
	}
}
