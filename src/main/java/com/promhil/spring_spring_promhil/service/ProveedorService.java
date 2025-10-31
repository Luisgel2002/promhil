package com.promhil.spring_spring_promhil.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promhil.spring_spring_promhil.model.Proveedor;
import com.promhil.spring_spring_promhil.repository.ProveedorRepository;

@Service
public class ProveedorService {

	@Autowired
	private ProveedorRepository proveedorRepository;
	
	public List<Proveedor> getAll() {
		return proveedorRepository.findAll();
	}
}
