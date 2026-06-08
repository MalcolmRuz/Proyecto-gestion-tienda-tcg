package com.gestion.tienda.tcg.pago.client;

import com.gestion.tienda.tcg.pago.dto.PedidoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "pedidos", url = "http://localhost:8083")
public interface PedidoClient {

    @GetMapping("/api/v1/pedidos/{id}")
    PedidoResponse obtenerPedidoPorId(@PathVariable("id") Long id);

    // ACTUALIZAR ESTADO
    @PutMapping("/api/v1/pedidos/{id}/estado")
    PedidoResponse actualizarEstadoPago(@PathVariable("id") Long id, @RequestParam("estado") String estado);

}



