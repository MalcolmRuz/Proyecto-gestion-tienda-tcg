package com.gestion.tienda.tcg.carrito.assemblers;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.carrito.controller.V2.CarritoHistorialControllerV2;
import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;

@Component
public class CarritoHistorialModelAssembler {

    public EntityModel<CarritoHistorialResponse> toModel(CarritoHistorialResponse response, Long idCarrito) {
        return EntityModel.of(response,
                linkTo(methodOn(CarritoHistorialControllerV2.class)
                        .obtenerHistorial(idCarrito))
                        .withSelfRel());
    }
}
