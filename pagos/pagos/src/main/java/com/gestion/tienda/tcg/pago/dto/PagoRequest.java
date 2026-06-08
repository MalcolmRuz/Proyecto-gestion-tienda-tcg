package com.gestion.tienda.tcg.pago.dto;

import com.gestion.tienda.tcg.pago.enums.MetodoPago;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagoRequest {

    @NotNull(message = "Debe ingresar el ID del carrito")
    private Long idCarrito;

    @NotNull(message = "El monto no debe ser nulo")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotNull(message = "Debe seleccionar un método de pago")
    private MetodoPago metodoPago;

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;
}