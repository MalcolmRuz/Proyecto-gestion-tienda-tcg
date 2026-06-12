package com.gestion_tienda_tcg.inventario.assemblers;

import com.gestion_tienda_tcg.inventario.controller.v2.InventarioControllerV2; // 👈 Cambiado a la V2
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<InventarioResponse, EntityModel<InventarioResponse>> {

    @Override
    public EntityModel<InventarioResponse> toModel(InventarioResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(InventarioControllerV2.class).stockPorProducto(response.getIdProducto())).withSelfRel(),
                linkTo(methodOn(InventarioControllerV2.class).listarInventariosConProductos()).withRel("todos-los-inventarios")
        );
    }

    public EntityModel<InventarioResponse> toModelWithStockLink(InventarioResponse response) {
        return toModel(response).add(
                linkTo(methodOn(InventarioControllerV2.class).stockPorProducto(response.getIdProducto())).withRel("ver-stock-actual")
        );
    }
}






