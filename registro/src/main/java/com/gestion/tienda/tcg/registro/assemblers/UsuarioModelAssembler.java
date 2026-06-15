package com.gestion.tienda.tcg.registro.assemblers;

import com.gestion.tienda.tcg.registro.controller.UsuarioControllerV2;
import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioResponse, EntityModel<UsuarioResponse>>{

    @Override
    public EntityModel<UsuarioResponse> toModel(UsuarioResponse response){

        return EntityModel.of(response,
                linkTo(methodOn(UsuarioControllerV2.class)
                        .buscarPorId(response.getIdUsuario()))
                        .withSelfRel(),

                linkTo(methodOn(UsuarioControllerV2.class)
                        .listar())
                        .withRel("listar-usuarios"),

                linkTo(methodOn(UsuarioControllerV2.class)
                        .registrar(null))
                        .withRel("registrar-usuario"),

                linkTo(methodOn(UsuarioControllerV2.class)
                        .actualizar(response.getIdUsuario(),null))
                        .withRel("actualizar-usuario"),

                linkTo(methodOn(UsuarioControllerV2.class)
                        .eliminar(response.getIdUsuario()))
                        .withRel("eliminar-usuario"),

                linkTo(methodOn(UsuarioControllerV2.class)
                        .convertirUsuarioAdmin(response.getIdUsuario()))
                        .withRel("convertir-admin")


        );

    }

}
