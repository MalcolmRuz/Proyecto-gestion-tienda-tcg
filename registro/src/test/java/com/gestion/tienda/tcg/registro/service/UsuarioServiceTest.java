package com.gestion.tienda.tcg.registro.service;

import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.mapper.UsuarioMapper;
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
}
