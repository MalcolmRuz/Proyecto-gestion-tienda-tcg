package com.gestion.tienda.tcg.pago.controller;

import com.gestion.tienda.tcg.pago.assemblers.PagoModelAssembler;
import com.gestion.tienda.tcg.pago.dto.PagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import com.gestion.tienda.tcg.pago.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/pagos")
public class PagoControllerV2 {

    private final PagoService pagoService;
    private final PagoModelAssembler assembler;

    public PagoControllerV2 (PagoService pagoService, PagoModelAssembler assembler){

        this.pagoService = pagoService;
        this.assembler = assembler;

    }

    @PostMapping
    public ResponseEntity<EntityModel<PagoResponse>>realizarPago(@Valid @RequestBody PagoRequest request){

        PagoResponse response = pagoService.realizarPago(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(response));

    }

    @GetMapping
    public CollectionModel<EntityModel<PagoResponse>>listadoPagos(){

        List<EntityModel<PagoResponse>> lista = pagoService.listarPagos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(lista,
                linkTo(methodOn(PagoControllerV2.class)
                        .listadoPagos())
                        .withSelfRel());

    }

    @GetMapping("/{id}")
    public EntityModel<PagoResponse> buscarPagoPorId(
            @PathVariable Long id){

        PagoResponse response = pagoService.buscarPagoPorId(id);

        return assembler.toModel(response);
    }

    @GetMapping("/estado/{estado}")
    public CollectionModel<EntityModel<PagoResponse>> listarPagosPorEstado(
            @PathVariable EstadoPago estado){

        List<EntityModel<PagoResponse>> lista =
                pagoService.listarPagosPorEstado(estado)
                        .stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(
                lista,
                linkTo(methodOn(PagoControllerV2.class)
                        .listarPagosPorEstado(estado))
                        .withSelfRel()
        );
    }

}
