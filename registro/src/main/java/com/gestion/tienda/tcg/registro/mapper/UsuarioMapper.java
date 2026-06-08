package com.gestion.tienda.tcg.registro.mapper;

import com.gestion.tienda.tcg.registro.dto.UsuarioRequest;
import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.model.Usuario;
import org.springframework.stereotype.Component;

@Component

public class UsuarioMapper {

    //Convierte un UsuarioRequest en una entidad Usuario, "recibe como JSON"
    public Usuario toEntity (UsuarioRequest request){

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setDireccion(request.getDireccion());
        usuario.setTipoDespacho(request.getTipoDespacho());

        return usuario;

    }
    //Convierte una entidad Usuario en un UsuarioResponse, "devuelve como JSON"
    public UsuarioResponse toResponse(Usuario usuario){

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getDireccion(),
                usuario.getRol().getNombreRol().name(),
                usuario.getTipoDespacho().name());

    }

}
