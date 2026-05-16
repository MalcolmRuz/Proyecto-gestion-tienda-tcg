package com.gestion_tienda_tcg.productos.client;

import com.gestion_tienda_tcg.productos.dto.InventarioRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "INVENTARIO-SERVICE")
    public interface InventarioClient {
        @PostMapping("/api/v1/inventarios") // Asumiendo que este es el endpoint en Inventario
        void inicializarInventario(@RequestBody InventarioRequest request);
    }

