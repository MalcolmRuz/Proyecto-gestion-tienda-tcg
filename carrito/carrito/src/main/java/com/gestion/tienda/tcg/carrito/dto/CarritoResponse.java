package com.gestion.tienda.tcg.carrito.dto;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponse {
    private Long idCarrito;
    private EstadoCarrito estadoCarrito;
    private Double totalCarrito;
    private List<CarritoItemResponse> items;
    private List<CarritoHistorialResponse> historial;

}
