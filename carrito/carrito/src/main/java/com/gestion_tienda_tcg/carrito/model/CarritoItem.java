package com.gestion_tienda_tcg.carrito.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    // Relacion M:1 con Carrito
    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonBackReference
    private Carrito carrito;

    /*
     * FK A MICROSERVICIO PRODUCTOS
     * 
     * @Column(name= "producto_id, nullable = false")
     * private Long productoId;
     */
}
