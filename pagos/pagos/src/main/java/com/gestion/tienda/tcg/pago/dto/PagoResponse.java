package com.gestion.tienda.tcg.pago.dto;

import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import com.gestion.tienda.tcg.pago.enums.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PagoResponse {

    private final Long idPago;
    private final Long idCarrito;
    private final Double monto;
    private final MetodoPago metodoPago;
    private final EstadoPago estado;
    private final LocalDateTime fechaPago;
    private final Double vuelto;
}