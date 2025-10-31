package com.promhil.spring_spring_promhil.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.promhil.spring_spring_promhil.dto.ResultadoResponse;
import com.promhil.spring_spring_promhil.model.Boleta;
import com.promhil.spring_spring_promhil.model.DetalleBoleta;
import com.promhil.spring_spring_promhil.repository.BoletaRepository;
import com.promhil.spring_spring_promhil.repository.DetalleBoletaRepository;

@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final DetalleBoletaRepository detalleBoletaRepository;

    public BoletaService(BoletaRepository boletaRepository,
                         DetalleBoletaRepository detalleBoletaRepository) {
        this.boletaRepository = boletaRepository;
        this.detalleBoletaRepository = detalleBoletaRepository;
    }

    @Transactional
    public ResultadoResponse create(Boleta boleta) {
        try {
            boleta.setFechaRegistro(LocalDateTime.now());

            List<DetalleBoleta> detallesDelController = boleta.getLstDetalleBoleta();

            boleta.setLstDetalleBoleta(null);

            Boleta cabecera = boletaRepository.save(boleta);
            Integer numBoletaGenerado = cabecera.getNumBoleta();

            if (detallesDelController != null) {
                for (DetalleBoleta det : detallesDelController) {

                    DetalleBoleta nuevo = new DetalleBoleta();
                    nuevo.setNumBoleta(numBoletaGenerado);
                    nuevo.setIdProducto(det.getIdProducto());

                    nuevo.setBoleta(cabecera);
                    nuevo.setProducto(det.getProducto());

                    nuevo.setCantidad(det.getCantidad());
                    nuevo.setPrecioVenta(det.getPrecioVenta());

                    detalleBoletaRepository.save(nuevo);
                }
            }

            String mensaje = String.format("Boleta registrada con número %s", numBoletaGenerado);
            return new ResultadoResponse(true, mensaje);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error en BoletaService: " + e.getMessage());
        }
    }

    public List<Boleta> getAll() {
        return boletaRepository.findAll();
    }
}
