package com.gestion.tienda.tcg.carrito.Client;

import com.gestion.tienda.tcg.carrito.dto.InventarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventario-service", url = "${urls.inventario.service}")
public interface InventarioClient {

    @PatchMapping("/api/v1/inventarios/{idProducto}/reducir/{cantidad}")
    InventarioResponse disminuirStock(
            @PathVariable("idProducto") Long idProducto,
            @PathVariable("cantidad") Integer cantidad);
}
