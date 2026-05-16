package com.gestion_tienda_tcg.inventario.client;


import com.gestion_tienda_tcg.inventario.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "producto-service")
public interface ProductoClient {
@GetMapping("/api/v1/productos/{id}")
    ProductoDto obtenerProductoPorId(@PathVariable("id") Long id);

}
