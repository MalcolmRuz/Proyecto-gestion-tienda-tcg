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

import com.gestion.tienda.tcg.pedido.assemblers.HistorialPedidoModelAssembler;
import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.service.HistorialPedidoService;

@RestController
@RequestMapping("/api/v2/historial")
public class HistorialPedidoControllerV2 {

    private final HistorialPedidoService historialService;
    private final HistorialPedidoModelAssembler assembler;

    public HistorialPedidoControllerV2(HistorialPedidoService historialService,
            HistorialPedidoModelAssembler assembler) {
        this.historialService = historialService;
        this.assembler = assembler;
    }

    @GetMapping("/pedido/{pedidoId}")
    public CollectionModel<EntityModel<HistorialPedidoResponse>> obtenerHistorial(@PathVariable Long pedidoId) {
        List<EntityModel<HistorialPedidoResponse>> lista = historialService.obtenerHistorial(pedidoId).stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(HistorialPedidoControllerV2.class).obtenerHistorial(pedidoId)).withSelfRel());
    }
}
