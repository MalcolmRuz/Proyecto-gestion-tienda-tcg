package com.gestion.tienda.tcg.carrito.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table (name ="carrito_historial")
public class CarritoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long idHistorial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarrito estado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column
    private String descripcion;

    //=========================
    //Relacion M:1 con Carrito
    //=========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonBackReference
    private Carrito carrito;

    //=========================
    //Fecha automática
    //=========================
    @PrePersist
    public void prePersist() {

        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }

}
