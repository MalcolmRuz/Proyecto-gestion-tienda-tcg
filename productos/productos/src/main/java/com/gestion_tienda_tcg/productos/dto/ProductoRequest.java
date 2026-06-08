package com.gestion_tienda_tcg.productos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {
    @NotBlank
    private String nombreProducto;
    @NotBlank
    private String descripcion;
    @NotNull
    private Boolean estado;
    @NotNull(message = "El proveedor es obligatorio")
    private Long idProveedor;
    @NotNull(message = "El precio es obligatorio")
    @Positive(message ="El precio debe ser mayor a 0")
    private Double precioUnitario;
}
