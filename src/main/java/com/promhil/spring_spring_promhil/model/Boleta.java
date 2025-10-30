package com.promhil.spring_spring_promhil.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_cab_boleta")
@Getter
@Setter
@DynamicInsert
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_boleta")
    private Integer numBoleta;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<DetalleBoleta> lstDetalleBoleta;

    public Double getTotal() {
        if (lstDetalleBoleta == null || lstDetalleBoleta.isEmpty()) {
            return 0.0;
        }
        return lstDetalleBoleta.stream()
                .mapToDouble(d -> d.getPrecioVenta() * d.getCantidad())
                .sum();
    }
}
