package com.gestion.tienda.tcg.carrito.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Relacion 1:M con CarritoItem
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CarritoItem> items;

    // Relacion M:1 con CarritoHistorial
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CarritoHistorial> historial;

    // Estado = Activo y total= 0.0 por defecto
    @PrePersist
    public void valoresPorDefecto() {
        if (this.estadoCarrito == null) {
            this.estadoCarrito = EstadoCarrito.ACTIVO;
        }
        if (this.totalCarrito == null) {
            this.totalCarrito = 0.0;
        }
    }
}
