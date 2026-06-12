package com.gestion.tienda.tcg.pedido.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.pedido.assemblers.PedidoModelAssembler;
import com.gestion.tienda.tcg.pedido.dto.CrearPedidoRequest;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.service.PedidoService;

@RestController
@RequestMapping("/api/v2/pedidos")
public class PedidoControllerV2 {

    private final PedidoService pedidoService;
    private final PedidoModelAssembler assembler;

    public PedidoControllerV2(PedidoService pedidoService, PedidoModelAssembler assembler) {
        this.pedidoService = pedidoService;
        this.assembler = assembler;
    }

    @PostMapping
    public EntityModel<PedidoResponse> crear(@RequestBody CrearPedidoRequest request) {
        PedidoResponse response = pedidoService.crearPedido(request.getIdCarrito(), request.getUsuarioId(),
                request.getDireccionEnvio());
        return assembler.toModel(response);
    }

    @GetMapping
    public CollectionModel<EntityModel<PedidoResponse>> listarTodos() {
        List<EntityModel<PedidoResponse>> lista = pedidoService.listarTodos().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(PedidoControllerV2.class).listarTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(pedidoService.buscarPorId(id));
    }

    @PutMapping("/{id}/estado")
    public EntityModel<PedidoResponse> actualizarEstado(@PathVariable Long id,
            @RequestParam EstadoPedido nuevoEstado) {
        return assembler.toModel(pedidoService.actualizarEstadoPedido(id, nuevoEstado));
    }
}
