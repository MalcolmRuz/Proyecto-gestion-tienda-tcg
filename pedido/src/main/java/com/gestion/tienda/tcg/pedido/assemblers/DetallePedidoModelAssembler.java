package com.gestion.tienda.tcg.pedido.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.controller.DetallePedidoControllerV2;
import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;

@Component
public class DetallePedidoModelAssembler
        implements RepresentationModelAssembler<DetallePedidoResponse, EntityModel<DetallePedidoResponse>> {

    @Override
    public @NonNull EntityModel<DetallePedidoResponse> toModel(@NonNull DetallePedidoResponse response) {
        return EntityModel.of(response,
                // El DTO no tiene pedidoId, así que solo devolvemos el recurso
                linkTo(methodOn(DetallePedidoControllerV2.class).listarPorPedido(null)).withRel("detalles-pedido"));
    }
}
