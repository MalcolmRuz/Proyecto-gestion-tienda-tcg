package com.gestion.tienda.tcg.carrito.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.carrito.controller.V2.CarritoItemControllerV2;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;

@Component
public class CarritoItemModelAssembler
                implements RepresentationModelAssembler<CarritoItemResponse, EntityModel<CarritoItemResponse>> {

        @Override
        public @NonNull EntityModel<CarritoItemResponse> toModel(@NonNull CarritoItemResponse response) {
                return EntityModel.of(response,
                                linkTo(methodOn(CarritoItemControllerV2.class)
                                                .obtenerItemDeCarrito(response.getIdCarrito(), response.getIdItem()))
                                                .withSelfRel(),
                                linkTo(methodOn(CarritoItemControllerV2.class)
                                                .listarItems(response.getIdCarrito()))
                                                .withRel("items"),
                                linkTo(methodOn(CarritoItemControllerV2.class)
                                                .actualizarCantidad(response.getIdItem(), null))
                                                .withRel("actualizar"),
                                linkTo(methodOn(CarritoItemControllerV2.class)
                                                .eliminarItem(response.getIdItem()))
                                                .withRel("eliminar"));
        }
}
