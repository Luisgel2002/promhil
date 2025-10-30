package com.promhil.spring_spring_promhil.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_categoria")
@Getter @Setter
public class Categoria {
	@Id
	@Column(name = "id_categoria")
	private Integer idCategoria;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	public String toString() {
		return descripcion;
	}
}
