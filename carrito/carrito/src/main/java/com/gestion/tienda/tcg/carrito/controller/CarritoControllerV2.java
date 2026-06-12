package com.gestion.tienda.tcg.carrito.controller;

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

import com.gestion.tienda.tcg.carrito.assemblers.CarritoModelAssembler;
import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.dto.PagarCarritoRequest;
import com.gestion.tienda.tcg.carrito.service.CarritoService;

@RestController
@RequestMapping("/api/v2/carritos")
public class CarritoControllerV2 {

    private final CarritoService carritoService;
    private final CarritoModelAssembler assembler;

    public CarritoControllerV2(CarritoService carritoService, CarritoModelAssembler assembler) {
        this.carritoService = carritoService;
        this.assembler = assembler;
    }

    @PostMapping
    public EntityModel<CarritoResponse> crearCarrito(@RequestBody CarritoRequest request) {
        return assembler.toModel(carritoService.crearCarrito(request));
    }

    @GetMapping
    public CollectionModel<EntityModel<CarritoResponse>> listar() {
        List<EntityModel<CarritoResponse>> lista = carritoService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(CarritoControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CarritoResponse> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(carritoService.buscarPorId(id));
    }

    @PostMapping("/{id}/pagar")
    public EntityModel<CarritoResponse> pagarCarrito(@PathVariable Long id, @RequestBody PagarCarritoRequest request) {
        return assembler.toModel(carritoService.pagarCarrito(id, request));
    }

    @PutMapping("/{id}/confirmar-pago")
    public EntityModel<CarritoResponse> confirmarPago(@PathVariable Long id, @RequestBody PagarCarritoRequest request) {
        return assembler.toModel(carritoService.confirmarPago(id, request.getUsuarioId()));
    }

    @PutMapping("/{id}/rechazar-pago")
    public EntityModel<CarritoResponse> rechazarPago(@PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean cancelar) {
        return assembler.toModel(carritoService.rechazarPago(id, cancelar));
    }

    @PostMapping("/{id}/cancelar")
    public EntityModel<CarritoResponse> cancelarCarrito(@PathVariable Long id) {
        return assembler.toModel(carritoService.cancelarCarrito(id));
    }

    @PostMapping("/{id}/reactivar")
    public EntityModel<CarritoResponse> reactivarCarrito(@PathVariable Long id) {
        return assembler.toModel(carritoService.reactivarCarrito(id));
    }
}
