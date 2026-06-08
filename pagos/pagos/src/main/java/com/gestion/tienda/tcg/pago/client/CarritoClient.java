package com.gestion.tienda.tcg.pago.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestion.tienda.tcg.pago.dto.CarritoResponse;
import com.gestion.tienda.tcg.pago.dto.ConfirmarPagoRequest;

@FeignClient(name = "carrito")
public interface CarritoClient {

        @GetMapping("/api/v1/carritos/{id}")
        CarritoResponse obtenerCarrito(@PathVariable("id") Long id);

        @PutMapping("/api/v1/carritos/{id}/confirmar-pago")
        CarritoResponse confirmarPago(
                        @PathVariable("id") Long id,
                        @RequestBody ConfirmarPagoRequest request);

        @PutMapping("/api/v1/carritos/{id}/rechazar-pago")
        CarritoResponse rechazarPago(
                        @PathVariable("id") Long id,
                        @RequestParam("cancelar") boolean cancelar);
}