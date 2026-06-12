package com.gestion_tienda_tcg.inventario.assemblers;

import com.gestion_tienda_tcg.inventario.controller.v2.InventarioControllerV2;
import com.gestion_tienda_tcg.inventario.dto.InventarioDetalleResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventarioDetalleModelAssembler implements RepresentationModelAssembler<InventarioDetalleResponse, EntityModel<InventarioDetalleResponse>> {

    @Override
    public EntityModel<InventarioDetalleResponse> toModel(InventarioDetalleResponse detalle) {
        return EntityModel.of(detalle,
                linkTo(methodOn(InventarioControllerV2.class).stockPorProducto(detalle.getIdProducto())).withSelfRel()
        );
    }
}
