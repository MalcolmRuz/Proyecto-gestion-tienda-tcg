package com.gestion.tienda.tcg.carrito.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carrito")

public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarrito estadoCarrito;

    @Column(nullable = false)
    private Double totalCarrito;

    //=========================
    //Relacion 1:M con CarritoItem
    //=========================
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CarritoItem> items;

    //=========================
    //Relacion 1:M con CarritoHistorial
    //=========================
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CarritoHistorial> historial;

    //=========================
    //Valores del carrito por defecto
    //=========================
    @PrePersist
    public void valoresPorDefecto(){
        if (this.estadoCarrito == null){
            this.estadoCarrito = EstadoCarrito.ACTIVO;
        }
        if (this.totalCarrito == null){
            this.totalCarrito = 0.0;
        }
    }

}
