package com.promhil.spring_spring_promhil.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.promhil.spring_spring_promhil.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByCuentaAndClave(String cuenta, String clave);

    List<Usuario> findAllByOrderByIdUsuarioAsc();
}
