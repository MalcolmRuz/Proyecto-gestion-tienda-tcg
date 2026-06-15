package com.gestion_tienda_tcg.productos.assemblers;

import com.gestion_tienda_tcg.productos.controller.v2.CategoriaControllerV2;
import com.gestion_tienda_tcg.productos.dto.CategoriaResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class CategoriaModelAssembler  implements  RepresentationModelAssembler<CategoriaResponse, EntityModel<CategoriaResponse>>
{
    @Override
    public EntityModel<CategoriaResponse> toModel(CategoriaResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerCategoriaPorId(response.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).listarCategorias()).withRel("categorias")
        );
    }

    @Override
    public CollectionModel<EntityModel<CategoriaResponse>> toCollectionModel(Iterable<? extends CategoriaResponse> responses) {
        var entityModels = StreamSupport.stream(responses.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(entityModels,
                linkTo(methodOn(CategoriaControllerV2.class).listarCategorias()).withSelfRel()
        );
    }







}
