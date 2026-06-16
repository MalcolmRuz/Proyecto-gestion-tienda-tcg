package com.gestion.tienda.tcg.registro.service;

import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.enums.NombreRol;
import com.gestion.tienda.tcg.registro.exception.RecursoNoEncontradoException;
import com.gestion.tienda.tcg.registro.mapper.UsuarioMapper;
import com.gestion.tienda.tcg.registro.model.Rol;
import com.gestion.tienda.tcg.registro.model.Usuario;
import com.gestion.tienda.tcg.registro.repository.RolRepository;
import com.gestion.tienda.tcg.registro.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void debeBuscarUsuarioPorId() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Gonzalo");

        UsuarioResponse response = new UsuarioResponse(
                1L,
                "Gonzalo",
                "go.urtubia@gmail.com",
                "Santa Margarita 1383",
                "ADMIN",
                "DOMICILIO");


        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(response);

        UsuarioResponse resultado =
                usuarioService.buscarPorId(1L);

        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("Gonzalo", resultado.getNombre());
        assertEquals("go.urtubia@gmail.com",resultado.getEmail());
        assertEquals("Santa Margarita 1383",resultado.getDireccion());
        assertEquals("ADMIN",resultado.getNombreRol());
        assertEquals("DOMICILIO",resultado.getTipoDespacho());

        verify(usuarioRepository).findById(1L);
    }

    @Test
    void lanzarExceptionCuandoUsuarioNoExiste() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.buscarPorId(1L)
        );

        verify(usuarioRepository).findById(1L);
    }

    @Test
    void debeEliminarUsuario() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Gonzalo");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        usuarioService.eliminarPorId(1L);

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void debeAsignarRolAdmin() {

        Rol rolCliente = new Rol();
        rolCliente.setIdRol(1L);
        rolCliente.setNombreRol(NombreRol.CLIENTE);

        Rol rolAdmin = new Rol();
        rolAdmin.setIdRol(2L);
        rolAdmin.setNombreRol(NombreRol.ADMIN);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Gonzalo");
        usuario.setRol(rolCliente);

        UsuarioResponse response = new UsuarioResponse(
                1L,
                "Gonzalo",
                "go.urtubia@gmail.com",
                "Santa Margarita 1383",
                "ADMIN",
                "DOMICILIO"
        );

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(rolRepository.findByNombreRol(NombreRol.ADMIN))
                .thenReturn(Optional.of(rolAdmin));

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(response);

        UsuarioResponse resultado =
                usuarioService.asignarRolAdminPorIdUsuario(1L);

        assertEquals("ADMIN", resultado.getNombreRol());

        verify(usuarioRepository).findById(1L);
        verify(rolRepository).findByNombreRol(NombreRol.ADMIN);
        verify(usuarioRepository).save(usuario);
    }

}
