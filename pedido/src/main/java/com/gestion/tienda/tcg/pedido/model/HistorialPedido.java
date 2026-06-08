package com.gestion.tienda.tcg.pedido.model;

import java.time.LocalDateTime;

import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historial_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;

    private String descripcion;

    private LocalDateTime fechaCambio = LocalDateTime.now();
}