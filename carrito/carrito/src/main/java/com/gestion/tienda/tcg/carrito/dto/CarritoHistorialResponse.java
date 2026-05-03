package com.gestion.tienda.tcg.carrito.dto;

import java.time.LocalDateTime;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoHistorialResponse {

    private Long idHistorial;

    private EstadoCarrito estado;

    private String descripcion;

    private LocalDateTime fecha;
}