package com.gestion.tienda.tcg.registro.controller;

import com.gestion.tienda.tcg.registro.assemblers.UsuarioModelAssembler;
import com.gestion.tienda.tcg.registro.dto.UsuarioRequest;
import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.service.UsuarioService;
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

    @PostMapping
    public ResponseEntity<EntityModel<UsuarioResponse>>registrar(@Valid @RequestBody UsuarioRequest request){

        UsuarioResponse response = usuarioService.registrarUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(response));
    }

    @PutMapping("/{id}/actualizar")
    public EntityModel<UsuarioResponse>actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request){

        UsuarioResponse response = usuarioService.actualizarUsuario(id,request);

        return assembler.toModel(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){

        usuarioService.eliminarPorId(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}/admin")
    public EntityModel<UsuarioResponse>convertirUsuarioAdmin(@PathVariable Long id){

        UsuarioResponse response = usuarioService.asignarRolAdminPorIdUsuario(id);

        return assembler.toModel(response);

    }

}
