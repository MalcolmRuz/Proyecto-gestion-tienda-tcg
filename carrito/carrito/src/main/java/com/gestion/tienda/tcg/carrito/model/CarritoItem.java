package com.gestion.tienda.tcg.carrito.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carrito_item")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double precioTotalItem;

        //=========================
        //Relacion 1:M con Carrito
        //=========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonBackReference
    private Carrito carrito;

    //=========================
    //FK lógica al microservicio producto
    //=========================
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
}
