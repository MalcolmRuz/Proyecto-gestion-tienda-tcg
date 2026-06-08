package com.gestion.tienda.tcg.pago.mapper;

import com.gestion.tienda.tcg.pago.dto.PagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import com.gestion.tienda.tcg.pago.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public Pago toEntity(PagoRequest request) {
        Pago pago = new Pago();
        pago.setIdCarrito(request.getIdCarrito());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        return pago;
    }

    public PagoResponse toResponse(Pago pago) {
        return new PagoResponse(
                pago.getIdPago(),
                pago.getIdCarrito(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getEstado(),
                pago.getFechaPago(),
                pago.getVuelto()
        );
    }
}

