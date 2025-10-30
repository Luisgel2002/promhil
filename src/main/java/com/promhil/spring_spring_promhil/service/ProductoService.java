package com.promhil.spring_spring_promhil.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promhil.spring_spring_promhil.dto.ProductoFilter;
import com.promhil.spring_spring_promhil.dto.ResultadoResponse;
import com.promhil.spring_spring_promhil.model.Producto;
import com.promhil.spring_spring_promhil.repository.ProductoRepository;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	public List<Producto> getAll() {
		return productoRepository.findAllByOrderByIdProductoDesc();
	}
	
	public List<Producto> search(ProductoFilter filter) {
		return productoRepository.findAllWithFilters(filter.getIdCategoria(), filter.getIdProveedor());
	}
	
	public ResultadoResponse create(Producto producto) {
		try {
			Producto productoRegistrado = productoRepository.save(producto);
			
			String mensaje = String.format("Producto registrado con Id %s", productoRegistrado.getIdProducto());
			return new ResultadoResponse(true, mensaje);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error en ProductoService: "+ e.getMessage());
		}
	}
	
	public Producto getOne(Integer id) {
		return productoRepository.findById(id).orElseThrow();
	}
	
	public ResultadoResponse update(Producto producto) {
		try {
			Producto productoRegistrado = productoRepository.save(producto);
			
			String mensaje = String.format("Producto actualizado con Id %s", productoRegistrado.getIdProducto());
			return new ResultadoResponse(true, mensaje);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error en ProductoService: "+ e.getMessage());
		}
	}
	
	public ResultadoResponse cambiarEstado(Integer id) {

		Producto producto = this.getOne(id);
		String accion = producto.getActivo() ? "desactivado" : "activado";

		producto.setActivo(!producto.getActivo());

		try {
			Producto registrado = productoRepository.save(producto);

			String mensaje = String.format("Producto con Id %s %s", registrado.getIdProducto(), accion);
			return new ResultadoResponse(true, mensaje);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResultadoResponse(false, "Error al cambiar de estado: " + ex.getMessage());
		}
	}
	
}
