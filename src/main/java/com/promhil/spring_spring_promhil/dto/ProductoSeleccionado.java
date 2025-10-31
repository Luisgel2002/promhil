package com.promhil.spring_spring_promhil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoSeleccionado {
	private Integer idProducto;
	private String descripcion;
	private double precio;
	private int cantidad;

	public Double getSubtotal() {
		return precio * cantidad;
	}
}
