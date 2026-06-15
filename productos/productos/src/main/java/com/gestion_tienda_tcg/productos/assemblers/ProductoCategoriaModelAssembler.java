package com.gestion_tienda_tcg.productos.assemblers;
import com.gestion_tienda_tcg.productos.controller.v2.ProductoCategoriaControllerV2;
import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoCategoriaModelAssembler implements RepresentationModelAssembler<ProductoCategoriaResponse, EntityModel<ProductoCategoriaResponse>> {

    @Override
    public EntityModel<ProductoCategoriaResponse> toModel(ProductoCategoriaResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(ProductoCategoriaControllerV2.class).listarPorCategoria(response.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(ProductoCategoriaControllerV2.class).listarTodasLasRelaciones()).withRel("todas-las-relaciones")
        );
    }

    @Override
    public CollectionModel<EntityModel<ProductoCategoriaResponse>> toCollectionModel(Iterable<? extends ProductoCategoriaResponse> responses) {
        var entityModels = StreamSupport.stream(responses.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(entityModels,
                linkTo(methodOn(ProductoCategoriaControllerV2.class).listarTodasLasRelaciones()).withSelfRel()
        );
    }
}