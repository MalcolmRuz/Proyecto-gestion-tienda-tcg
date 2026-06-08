package com.gestion.tienda.tcg.pago.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponse {

    private Long idCarrito;
    private String estadoCarrito;
    private Double totalCarrito;
    private List<CarritoItemResponse> items;

    // 3. 🔽 AGREGAMOS EL CAMPO QUE FALTA 🔽
    private String direccionEnvio;
}