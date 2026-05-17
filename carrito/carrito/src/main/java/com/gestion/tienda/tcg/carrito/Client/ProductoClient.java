package com.gestion.tienda.tcg.carrito.Client;

import com.gestion.tienda.tcg.carrito.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service", url = "${urls.producto.service}")
public interface ProductoClient {
    @GetMapping("/api/v1/productos/{id}")
    ProductoDto obtenerProductoPorId(
            @PathVariable("id") Long id);
}
