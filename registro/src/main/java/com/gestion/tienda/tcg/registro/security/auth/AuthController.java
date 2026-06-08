package com.gestion.tienda.tcg.registro.security.auth;

import com.gestion.tienda.tcg.registro.model.Usuario;
import com.gestion.tienda.tcg.registro.repository.UsuarioRepository;
import com.gestion.tienda.tcg.registro.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){

            throw new RuntimeException("Contraseña incorrecta");

        }

        String token = jwtService.generateToken(
                usuario.getEmail(),
                usuario.getRol().getNombreRol().name()
        );

        return new LoginResponse(token);

    }

}