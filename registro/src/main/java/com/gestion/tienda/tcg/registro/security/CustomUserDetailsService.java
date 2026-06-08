package com.gestion.tienda.tcg.registro.security;

import com.gestion.tienda.tcg.registro.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class  CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // Servicio encargado de cargar usuarios
    // durante el proceso de autenticación
    public CustomUserDetailsService(UsuarioRepository usuarioRepository){

        this.usuarioRepository = usuarioRepository;

    }

    // Busca un usuario por email y lo retorna
    // como UserDetails para Spring Security
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"));

    }

}