package com.gestion.tienda.tcg.carrito.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventarioSimpleDto {

    private Long idInventario;

    @JsonProperty("idProducto")
    private Long productoId;

    private String nombreProducto;

    private Integer stockActual;

    private LocalDateTime fechaInventario;
}