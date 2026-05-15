package com.gestion_tienda_tcg.productos.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventarioRequest {

    private Integer stockActual;

    private Long idProducto;
    }

