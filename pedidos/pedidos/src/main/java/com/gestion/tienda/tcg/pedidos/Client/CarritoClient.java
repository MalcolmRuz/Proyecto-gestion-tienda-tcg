package com.gestion.tienda.tcg.pedidos.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestion.tienda.tcg.pedidos.dto.CarritoDto;

@FeignClient(name = "carrito-service", url = "${urls.carrito-service}")
public interface CarritoClient {

    @GetMapping("/api/v1/carritos/{id}")
    CarritoDto obtenerCarrito(@PathVariable("id") Long id);

    @PatchMapping("/api/v1/carritos/{id}/estado")
    void actualizarEstado(
            @PathVariable("id") Long id,
            @RequestParam("estado") String estado);
}