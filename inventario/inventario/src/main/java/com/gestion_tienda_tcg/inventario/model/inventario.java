package com.gestion_tienda_tcg.inventario.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class inventario {
    private String idInventario;
    private int stockActual;
    private LocalDate fechaInventario;
}
