package com.gestion.tienda.tcg.carrito.controller;

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

import com.gestion.tienda.tcg.carrito.assemblers.CarritoHistorialModelAssembler;
import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.service.CarritoHistorialService;

@RestController
@RequestMapping("/api/v2/carritos/historial")
public class CarritoHistorialControllerV2 {

    private final CarritoHistorialService historialService;
    private final CarritoHistorialModelAssembler assembler;

    public CarritoHistorialControllerV2(CarritoHistorialService historialService,
            CarritoHistorialModelAssembler assembler) {
        this.historialService = historialService;
        this.assembler = assembler;
    }

    @GetMapping("/{idCarrito}")
    public CollectionModel<EntityModel<CarritoHistorialResponse>> obtenerHistorial(@PathVariable Long idCarrito) {
        List<EntityModel<CarritoHistorialResponse>> lista = historialService.obtenerHistorial(idCarrito).stream()
                .map(historial -> assembler.toModel(historial, idCarrito)) // 👈 aquí pasas el idCarrito
                .collect(Collectors.toList());

        return CollectionModel.of(lista,
                linkTo(methodOn(CarritoHistorialControllerV2.class).obtenerHistorial(idCarrito)).withSelfRel());
    }

}
