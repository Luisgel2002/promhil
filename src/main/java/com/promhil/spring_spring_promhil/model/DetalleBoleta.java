package com.promhil.spring_spring_promhil.model;

import com.promhil.spring_spring_promhil.dto.DetalleBoletaId;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_det_boleta")
@IdClass(DetalleBoletaId.class)
@Getter
@Setter
public class DetalleBoleta {

    @Id
    @Column(name = "num_boleta")
    private Integer numBoleta;

    @Id
    @Column(name = "id_producto")
    private Integer idProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num_boleta", insertable = false, updatable = false)
    private Boleta boleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;
}
