package com.gestion.tienda.tcg.carrito.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.carrito.controller.V2.CarritoControllerV2;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;

@Component
public class CarritoModelAssembler
                implements RepresentationModelAssembler<CarritoResponse, EntityModel<CarritoResponse>> {

        @Override
        public @NonNull EntityModel<CarritoResponse> toModel(@NonNull CarritoResponse response) {
                return EntityModel.of(response,
                                linkTo(methodOn(CarritoControllerV2.class).buscarPorId(response.getIdCarrito()))
                                                .withSelfRel(),
                                linkTo(methodOn(CarritoControllerV2.class).listar()).withRel("carritos"),
                                linkTo(methodOn(CarritoControllerV2.class).pagarCarrito(response.getIdCarrito(), null))
                                                .withRel("pagar"),
                                linkTo(methodOn(CarritoControllerV2.class).cancelarCarrito(response.getIdCarrito()))
                                                .withRel("cancelar"),
                                linkTo(methodOn(CarritoControllerV2.class).reactivarCarrito(response.getIdCarrito()))
                                                .withRel("reactivar"));
        }
}
