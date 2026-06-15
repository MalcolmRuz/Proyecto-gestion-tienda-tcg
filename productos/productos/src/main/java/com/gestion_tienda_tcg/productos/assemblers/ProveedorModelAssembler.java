package com.gestion_tienda_tcg.productos.assemblers;

import com.gestion_tienda_tcg.productos.controller.v2.ProveedorControllerV2;
import com.gestion_tienda_tcg.productos.dto.ProveedorResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProveedorModelAssembler implements RepresentationModelAssembler<ProveedorResponse, EntityModel<ProveedorResponse>> {

    @Override
    public EntityModel<ProveedorResponse> toModel(ProveedorResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(ProveedorControllerV2.class).obtenerProveedorPorId(response.getIdProveedor())).withSelfRel(),

                // Link de acción: apunta al endpoint para actualizar el contacto de este proveedor específico
                linkTo(methodOn(ProveedorControllerV2.class).actualizarContacto(response.getIdProveedor(), null)).withRel("actualizar-contacto"),

                // Link rel: para navegar de vuelta a la lista completa de proveedores
                linkTo(methodOn(ProveedorControllerV2.class).listarProveedores()).withRel("proveedores")
        );
    }

    @Override
    public CollectionModel<EntityModel<ProveedorResponse>> toCollectionModel(Iterable<? extends ProveedorResponse> responses) {
        var entityModels = StreamSupport.stream(responses.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(entityModels,
                linkTo(methodOn(ProveedorControllerV2.class).listarProveedores()).withSelfRel()
        );
    }
}