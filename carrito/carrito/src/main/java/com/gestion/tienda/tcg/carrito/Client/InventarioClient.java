package com.gestion.tienda.tcg.carrito.Client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.gestion.tienda.tcg.carrito.dto.InventarioResponse;
import com.gestion.tienda.tcg.carrito.dto.InventarioSimpleDto;

@FeignClient(name = "INVENTARIO-SERVICE")
public interface InventarioClient {

        @PutMapping("/api/v1/inventarios/{idProducto}/reducir/{cantidad}")
        InventarioResponse disminuirStock(
                        @PathVariable("idProducto") Long idProducto,
                        @PathVariable("cantidad") Integer cantidad);

        @GetMapping("/api/v1/inventarios/producto/{idProducto}")
        InventarioResponse obtenerInventarioPorProducto(
                        @PathVariable("idProducto") Long idProducto);

        @GetMapping("/api/v1/inventarios/simple")
        List<InventarioSimpleDto> listarInventariosSimple();
}
