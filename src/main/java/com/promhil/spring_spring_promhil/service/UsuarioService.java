package com.promhil.spring_spring_promhil.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promhil.spring_spring_promhil.model.Tipo;
import com.promhil.spring_spring_promhil.model.Usuario;
import com.promhil.spring_spring_promhil.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getOne(Integer id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAllByOrderByIdUsuarioAsc();
    }


    public Usuario guardar(Usuario usuario) {

        if (usuario.getIdUsuario() != null) {
            Usuario actual = usuarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow();

            if (usuario.getClave() == null || usuario.getClave().isBlank()) {
                usuario.setClave(actual.getClave());
            }
        }

        return usuarioRepository.save(usuario);
    }

    public void cambiarEstado(Integer id) {
        Usuario u = getOne(id);
        u.setActivo(u.getActivo() == null ? Boolean.TRUE : !u.getActivo());
        usuarioRepository.save(u);
    }

    public Tipo construirTipo(Integer idTipo) {
        Tipo t = new Tipo();
        t.setIdTipo(idTipo);
        return t;
    }
}
