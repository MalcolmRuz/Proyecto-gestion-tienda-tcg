package com.gestion.tienda.tcg.registro.service;

import com.gestion.tienda.tcg.registro.dto.UsuarioRequest;
import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.enums.NombreRol;
import com.gestion.tienda.tcg.registro.enums.TipoDespacho;
import com.gestion.tienda.tcg.registro.exception.RecursoNoEncontradoException;
import com.gestion.tienda.tcg.registro.mapper.UsuarioMapper;
import com.gestion.tienda.tcg.registro.model.Rol;
import com.gestion.tienda.tcg.registro.model.Usuario;
import com.gestion.tienda.tcg.registro.repository.RolRepository;
import com.gestion.tienda.tcg.registro.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, RolRepository rolRepository, PasswordEncoder passwordEncoder){

        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional

    public UsuarioResponse registrarUsuario (UsuarioRequest request){

        Usuario usuarioCreado = usuarioMapper.toEntity(request);//convierte un DTO(UsuarioRequest) a ENTITY

        usuarioCreado.setPassword(passwordEncoder.encode(request.getPassword()));

        Rol rolAsiganado = rolRepository.findByNombreRol(NombreRol.CLIENTE)
                .orElseThrow(()-> new RecursoNoEncontradoException("Rol no asignado"));

        usuarioCreado.setFechaCreacion(LocalDate.now());

        usuarioCreado.setRol(rolAsiganado);

        if(usuarioCreado.getTipoDespacho() == TipoDespacho.DOMICILIO
                && (usuarioCreado.getDireccion()==null
                || usuarioCreado.getDireccion().isBlank())){

            throw new IllegalArgumentException("Debe ingresar dirección para despacho a domicilio");

        }

        Usuario usuarioGuardado = usuarioRepository.save(usuarioCreado);

        return usuarioMapper.toResponse(usuarioGuardado);

    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id){

        Usuario usuarioEncontrado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encotrado"));

        return  usuarioMapper.toResponse(usuarioEncontrado);
    }

    public List <UsuarioResponse> listarUsuarios(){

        List<Usuario> usuarios = usuarioRepository.findAll();//findAll() -> trae todas las filas en la base de datos como entidades

        return usuarios.stream()//.stream().map() -> convierte cada usuario en UsuarioResponse
                .map(usuarioMapper::toResponse)
                .toList(); //devuelve una lista de DTOs

    }

    @Transactional
    public void eliminarPorId(Long id){

        Usuario usuarioEncontrado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encotrado"));

        usuarioRepository.delete(usuarioEncontrado);

    }

    @Transactional
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setPassword(request.getPassword());
        usuario.setEmail(request.getEmail());
        usuario.setDireccion(request.getDireccion());
        usuario.setTipoDespacho(request.getTipoDespacho());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuarioGuardado);

    }

    @Transactional

    public UsuarioResponse asignarRolAdminPorIdUsuario(Long id){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("Usuario no encontrado"));

        if(usuario.getRol().getNombreRol() == NombreRol.ADMIN) {

            throw new IllegalArgumentException("El usuario ya es ADMIN");

        }

        Rol rolAdmin = rolRepository.findByNombreRol(NombreRol.ADMIN)
                .orElseThrow(()-> new RecursoNoEncontradoException("Rol no asignado"));

        usuario.setRol(rolAdmin);

        usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuario);

    }
}
