package com.gestion_tienda_tcg.carrito.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gestion_tienda_tcg.carrito.dto.ProductoDto;

@FeignClient(name = "producto-service", url = "${urls.producto-service}")
public interface ProductoClient {

    @GetMapping("/api/v1/productos/{id}")
    ProductoDto obtenerProductoPorId(@PathVariable("id") Long id);

}
