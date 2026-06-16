package com.gestion.tienda.tcg.registro.controller;

import com.gestion.tienda.tcg.registro.assemblers.UsuarioModelAssembler;
import com.gestion.tienda.tcg.registro.dto.UsuarioRequest;
import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.service.UsuarioService;
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
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Registro HATEOAS", description = "Gestión de registros utilizando Spring HATEOAS")
public class UsuarioControllerV2 {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    public UsuarioControllerV2(UsuarioService usuarioService, UsuarioModelAssembler assembler){

        this.usuarioService = usuarioService;
        this.assembler = assembler;


    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Búsqueda de un usuario mediante su ID con enlaces HATEOAS")
    public EntityModel <UsuarioResponse> buscarPorId(@PathVariable Long id){

        UsuarioResponse response = usuarioService.buscarPorId(id);

        return assembler.toModel(response);

    }

    @GetMapping
    @Operation(summary = "Mostrar todos los usuarios", description = "Obtiene un listado de usuarios con enlaces HATEOAS")
    public CollectionModel<EntityModel<UsuarioResponse>> listar(){

        List<EntityModel<UsuarioResponse>> lista = usuarioService.listarUsuarios().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(lista,
                linkTo(methodOn(UsuarioControllerV2.class).listar()).withSelfRel());

    }

    @PostMapping
    @Operation(summary = "Registro de usuario", description = "Registra un nuevo usuario y retorna con enlaces HATEOAS")
    public ResponseEntity<EntityModel<UsuarioResponse>>registrar(@Valid @RequestBody UsuarioRequest request){

        UsuarioResponse response = usuarioService.registrarUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assembler.toModel(response));
    }

    @PutMapping("/{id}/actualizar")
    @Operation(summary = "Actualización de Datos", description = "Actualiza la información de un usuario y retorna con enlaces HATEOAS")
    public EntityModel<UsuarioResponse>actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request){

        UsuarioResponse response = usuarioService.actualizarUsuario(id,request);

        return assembler.toModel(response);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente mediante su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){

        usuarioService.eliminarPorId(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}/admin")
    @Operation(summary = "Asignar rol administrador", description = "Convierte un usuario con rol CLIENTE en ADMIN y retorna con enlaces HATEOAS")
    public EntityModel<UsuarioResponse>convertirUsuarioAdmin(@PathVariable Long id){

        UsuarioResponse response = usuarioService.asignarRolAdminPorIdUsuario(id);

        return assembler.toModel(response);

    }

}
