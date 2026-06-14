package com.gestion.tienda.tcg.pedido.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.controller.V2.HistorialPedidoControllerV2;
import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;

@Component
public class HistorialPedidoModelAssembler
        implements RepresentationModelAssembler<HistorialPedidoResponse, EntityModel<HistorialPedidoResponse>> {

    @Override
    public @NonNull EntityModel<HistorialPedidoResponse> toModel(@NonNull HistorialPedidoResponse response) {
        return EntityModel.of(response,
                // No hay pedidoId en el DTO, así que solo devolvemos el recurso
                linkTo(methodOn(HistorialPedidoControllerV2.class).obtenerHistorial(null)).withRel("historial-pedido"));
    }
}
