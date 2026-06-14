package com.gestion.tienda.tcg.pedido.controller.V2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.assemblers.DetallePedidoModelAssembler;
import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.service.DetallePedidoService;

@RestController
@RequestMapping("/api/v2/detalles")
public class DetallePedidoControllerV2 {

    private final DetallePedidoService detalleService;
    private final DetallePedidoModelAssembler assembler;

    public DetallePedidoControllerV2(DetallePedidoService detalleService, DetallePedidoModelAssembler assembler) {
        this.detalleService = detalleService;
        this.assembler = assembler;
    }

    @GetMapping("/pedido/{pedidoId}")
    public CollectionModel<EntityModel<DetallePedidoResponse>> listarPorPedido(@PathVariable Long pedidoId) {
        List<EntityModel<DetallePedidoResponse>> lista = detalleService.listarPorPedido(pedidoId).stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(DetallePedidoControllerV2.class).listarPorPedido(pedidoId)).withSelfRel());
    }
}
