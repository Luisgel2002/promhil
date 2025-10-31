package com.promhil.spring_spring_promhil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promhil.spring_spring_promhil.dto.AutenticacionFilter;
import com.promhil.spring_spring_promhil.model.Usuario;
import com.promhil.spring_spring_promhil.repository.UsuarioRepository;

@Service
public class AutenticacionService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario autenticar(AutenticacionFilter filter) {
		return usuarioRepository.findByCuentaAndClave(filter.getCuenta(), filter.getClave());
	}

}
