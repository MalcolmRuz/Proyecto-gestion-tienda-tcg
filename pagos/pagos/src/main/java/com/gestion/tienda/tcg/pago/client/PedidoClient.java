package com.gestion.tienda.tcg.pago.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PEDIDO-SERVICE")
public interface PedidoClient {

    @PostMapping("/{idCarrito}")
    Object crearPedidoDesdePago(
            @PathVariable("idCarrito") Long idCarrito,
            @RequestBody Object requestBody);
}