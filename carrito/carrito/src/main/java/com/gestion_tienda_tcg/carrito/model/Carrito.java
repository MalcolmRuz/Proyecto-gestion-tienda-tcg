package com.gestion_tienda_tcg.carrito.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Carrito {
    private char estado_carrito;
    private Integer id_carrito;
}
