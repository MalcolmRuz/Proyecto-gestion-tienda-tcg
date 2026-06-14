package com.gestion.tienda.tcg.carrito.controller.V2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.carrito.assemblers.CarritoItemModelAssembler;
import com.gestion.tienda.tcg.carrito.dto.ActualizarCantidadRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.service.CarritoItemService;

@RestController
@RequestMapping("/api/v2/carritos")
public class CarritoItemControllerV2 {

    private final CarritoItemService itemService;
    private final CarritoItemModelAssembler assembler;

    public CarritoItemControllerV2(CarritoItemService itemService, CarritoItemModelAssembler assembler) {
        this.itemService = itemService;
        this.assembler = assembler;
    }

    @PostMapping("/{idCarrito}/items")
    public EntityModel<CarritoItemResponse> agregarItem(@PathVariable Long idCarrito,
            @RequestBody CarritoItemRequest request) {
        return assembler.toModel(itemService.agregarItem(idCarrito, request));
    }

    @GetMapping("/{idCarrito}/items")
    public CollectionModel<EntityModel<CarritoItemResponse>> listarItems(@PathVariable Long idCarrito) {
        List<EntityModel<CarritoItemResponse>> lista = itemService.listarPorCarrito(idCarrito).stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista,
                linkTo(methodOn(CarritoItemControllerV2.class).listarItems(idCarrito)).withSelfRel());
    }

    @GetMapping("/{idCarrito}/items/{idItem}")
    public EntityModel<CarritoItemResponse> obtenerItemDeCarrito(@PathVariable Long idCarrito,
            @PathVariable Long idItem) {
        return assembler.toModel(itemService.buscarPorIdEnCarrito(idCarrito, idItem));
    }

    @PatchMapping("/items/{idItem}")
    public EntityModel<CarritoItemResponse> actualizarCantidad(@PathVariable Long idItem,
            @RequestBody ActualizarCantidadRequest request) {
        return assembler.toModel(itemService.actualizarCantidad(idItem, request.getCantidad()));
    }

    @DeleteMapping("/items/{idItem}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long idItem) {
        itemService.eliminarItem(idItem);
        return ResponseEntity.noContent().build();
    }
}
