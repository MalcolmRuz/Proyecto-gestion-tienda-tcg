package com.gestion.tienda.tcg.carrito.dto;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoHistorialResponse {

    private Long idHistorial;
    private EstadoCarrito estado;
    private String descripcion;
    private LocalDateTime fecha;

}
