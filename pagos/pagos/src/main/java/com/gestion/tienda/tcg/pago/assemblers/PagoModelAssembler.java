package com.gestion.tienda.tcg.pago.assemblers;

import com.gestion.tienda.tcg.pago.controller.PagoControllerV2;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler <PagoResponse, EntityModel<PagoResponse>> {

    @Override
    public EntityModel<PagoResponse>toModel(PagoResponse response){

        return EntityModel.of(response,

                linkTo(methodOn(PagoControllerV2.class)
                        .realizarPago(null))
                        .withRel("realizar-pago"),

                linkTo(methodOn(PagoControllerV2.class)
                        .listadoPagos())
                        .withRel("listado-pagos")

        );

    }

}
