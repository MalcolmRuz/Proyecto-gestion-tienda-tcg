package com.gestion.tienda.tcg.registro.controller;

import com.gestion.tienda.tcg.registro.assemblers.UsuarioModelAssembler;
import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.service.UsuarioService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerV2 {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    public UsuarioControllerV2(UsuarioService usuarioService, UsuarioModelAssembler assembler){

        this.usuarioService = usuarioService;
        this.assembler = assembler;


    }

    @GetMapping("/{id}")
    public EntityModel <UsuarioResponse> buscarPorId(@PathVariable Long id){

        UsuarioResponse response = usuarioService.buscarPorId(id);

        return assembler.toModel(response);

    }

    @GetMapping
    public CollectionModel<EntityModel<UsuarioResponse>> listar(){

        List<EntityModel<UsuarioResponse>> lista = usuarioService.listarUsuarios().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(lista,
                linkTo(methodOn(UsuarioControllerV2.class).listar()).withSelfRel());

    }

}
