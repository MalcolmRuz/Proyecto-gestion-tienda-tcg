package com.gestion_tienda_tcg.inventario.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {
    private String idInventario;
    private int stockActual;
    private LocalDate fechaInventario;
}
