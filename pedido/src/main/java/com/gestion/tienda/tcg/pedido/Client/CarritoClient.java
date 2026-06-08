package com.gestion.tienda.tcg.pedido.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestion.tienda.tcg.pedido.dto.CarritoDto;

// Definimos explícitamente el nombre del servicio y la URL donde correrá en tu Laragon
@FeignClient(name = "CARRITO")
public interface CarritoClient {

        @GetMapping("/api/v1/carritos/{id}")
        CarritoDto obtenerCarrito(
                        @PathVariable("id") Long id);

        @PatchMapping("/api/v1/carritos/{id}/estado")
        void actualizarEstado(
                        @PathVariable("id") Long id,
                        @RequestParam("nuevoEstado") String nuevoEstado);
}