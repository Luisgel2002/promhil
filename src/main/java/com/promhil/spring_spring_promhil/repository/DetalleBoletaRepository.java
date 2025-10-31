package com.promhil.spring_spring_promhil.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.promhil.spring_spring_promhil.dto.DetalleBoletaId;
import com.promhil.spring_spring_promhil.model.DetalleBoleta;

public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, DetalleBoletaId> {
}
