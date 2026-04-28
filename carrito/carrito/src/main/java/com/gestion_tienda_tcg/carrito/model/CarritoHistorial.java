package com.gestion_tienda_tcg.carrito.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carrito_historial")
public class CarritoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    @Column(name = "estado") // ACTIVO, PAGADO, ANULADO, CANCELADO
    @Enumerated(EnumType.STRING)
    private EstadoCarrito estado;

    @Column(name = "fecha_hist_carrito", nullable = false)
    private LocalDateTime fechaCarrito;

    @Column(name = "carrito_id", nullable = false)
    private Long carritoId;

}
