package com.gestion_tienda_tcg.carrito.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gestion_tienda_tcg.carrito.enums.EstadoCarrito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carrito_historial")
public class CarritoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarrito estado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    // Relacion M:1 con Carrito
    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonBackReference
    private Carrito carrito;

    // Fechas automaticas
    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }
}
