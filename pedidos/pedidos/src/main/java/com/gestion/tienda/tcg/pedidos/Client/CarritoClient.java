package com.gestion.tienda.tcg.pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gestion.tienda.tcg.pedidos.dto.external.CarritoResponseDto;

@FeignClient(name = "carrito-service", url = "${urls.carrito-service}")
public interface CarritoClient {

    @GetMapping("/api/v1/carritos/{id}")
    CarritoResponseDto obtenerCarritoPorId(
            @PathVariable("id") Long id);
}