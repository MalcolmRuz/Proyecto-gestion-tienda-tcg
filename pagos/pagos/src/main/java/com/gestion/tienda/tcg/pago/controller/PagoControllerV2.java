package com.gestion.tienda.tcg.pago.controller;

import com.gestion.tienda.tcg.pago.assemblers.PagoModelAssembler;
import com.gestion.tienda.tcg.pago.dto.PagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import com.gestion.tienda.tcg.pago.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pagos HATEOAS", description = "Gestión de pagos utilizando Spring HATEOAS")
public class PagoControllerV2 {

    private final PagoService pagoService;
    private final PagoModelAssembler assembler;

    public PagoControllerV2 (PagoService pagoService, PagoModelAssembler assembler){

        this.pagoService = pagoService;
        this.assembler = assembler;

    }

    @PostMapping
    @Operation(summary = "Realizar un pago", description = "Realiza un pago y retorna con enlaces HATEOAS")
    public ResponseEntity<EntityModel<PagoResponse>>realizarPago(@Valid @RequestBody PagoRequest request){

        PagoResponse response = pagoService.realizarPago(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(response));

    }

    @GetMapping
    @Operation(summary = "Mostrar todos los pagos", description = "Obtiene todos los pagos registrados con enlaces HATEOAS")
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
    @Operation(summary = "Buscar pagos por su ID", description = "Obtiene un pago específico mediante su ID con enlaces HATEOAS")
    public EntityModel<PagoResponse> buscarPagoPorId(@PathVariable Long id){

        PagoResponse response = pagoService.buscarPagoPorId(id);

        return assembler.toModel(response);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Mostrar pagos por su estado", description = "Obtiene todos los pagos filtrados por estado con enlaces HATEOAS")
    public CollectionModel<EntityModel<PagoResponse>> listarPagosPorEstado(@PathVariable EstadoPago estado){

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
