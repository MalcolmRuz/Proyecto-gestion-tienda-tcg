package com.gestion_tienda_tcg.productos.assemblers;

import com.gestion_tienda_tcg.productos.controller.v2.ProductoControllerV2;
import com.gestion_tienda_tcg.productos.dto.ProductoResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoResponse, EntityModel<ProductoResponse>> {

    @Override
    public EntityModel<ProductoResponse> toModel(ProductoResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(ProductoControllerV2.class).obtenerDetalle(response.getIdProducto())).withSelfRel(),

                linkTo(methodOn(ProductoControllerV2.class).desactivarProducto(response.getIdProducto())).withRel("desactivar"),

                linkTo(methodOn(ProductoControllerV2.class).listarPorProveedor(response.getIdProveedor())).withRel("productos-proveedor")
        );
    }

    @Override
    public CollectionModel<EntityModel<ProductoResponse>> toCollectionModel(Iterable<? extends ProductoResponse> responses) {
        var entityModels = StreamSupport.stream(responses.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(entityModels);
    }
}