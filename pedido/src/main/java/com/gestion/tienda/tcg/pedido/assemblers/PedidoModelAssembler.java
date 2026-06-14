package com.gestion.tienda.tcg.pedido.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.controller.V2.PedidoControllerV2;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<PedidoResponse, EntityModel<PedidoResponse>> {

    @Override
    public @NonNull EntityModel<PedidoResponse> toModel(@NonNull PedidoResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(PedidoControllerV2.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(PedidoControllerV2.class).listarTodos()).withRel("pedidos"),
                linkTo(methodOn(PedidoControllerV2.class).actualizarEstado(response.getId(), null))
                        .withRel("actualizar-estado"));
    }
}
