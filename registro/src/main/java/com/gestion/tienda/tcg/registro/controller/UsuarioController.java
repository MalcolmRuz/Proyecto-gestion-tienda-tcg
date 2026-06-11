package com.gestion.tienda.tcg.registro.controller;

import com.gestion.tienda.tcg.registro.dto.UsuarioRequest;
import com.gestion.tienda.tcg.registro.dto.UsuarioResponse;
import com.gestion.tienda.tcg.registro.exception.RecursoNoEncontradoException;
import com.gestion.tienda.tcg.registro.model.Usuario;
import com.gestion.tienda.tcg.registro.repository.UsuarioRepository;
import com.gestion.tienda.tcg.registro.security.auth.LoginRequest;
import com.gestion.tienda.tcg.registro.security.jwt.JwtService;
import com.gestion.tienda.tcg.registro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Registro", description = "Operaciones relacionadas con el sistema de registro")

//Crear → 201 status - body
//Obtener → 200 ok
//Actualizar → 200 ok
//Eliminar → 204 no content - build

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController (UsuarioService usuarioService,
                              UsuarioRepository usuarioRepository,
                              JwtService jwtService,
                              PasswordEncoder passwordEncoder){

        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/auth/login")
    @Operation(summary = "Inicio de sesión Admin", description = "Iniciar sesión como rol ADMIN para generar" +
                                                                 " un token JWT y acceder a métodos protegidos")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){

        System.out.println(request.getEmail());
        System.out.println(request.getPassword());

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        if(!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(
                usuario.getEmail(),
                usuario.getRol().getNombreRol().name()
        );

        return ResponseEntity.ok(token);

    }

    @PostMapping//status uso de POST, devolver 201 CREATED por que crea un recurso nuevo
    @Operation(summary = "Registro de usuario",description = "Registrar a un usuario como rol predeterminado cliente")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request){

        UsuarioResponse usuarioResponse = usuarioService.registrarUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse);

    }

    @GetMapping("/{id}")//para búsqueda por Id, uso de GET, 200 ok, porque solo está consultando
    @Operation(summary = "Búsqueda de Usuario", description = "Se busca a un usuario registrado a través de su id")
    public ResponseEntity<UsuarioResponse>buscarPorId(@PathVariable Long id){

        UsuarioResponse usuarioResponse = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(usuarioResponse);

    }

    @GetMapping
    @Operation(summary = "Mostrar todos los Usuarios", description = "Lista de todos los usuarios registrados")
    public ResponseEntity<List<UsuarioResponse>>listar(){

        List<UsuarioResponse> lista = usuarioService.listarUsuarios();

        return ResponseEntity.ok(lista);

    }

    @PutMapping("/{id}/actualizar")
    @Operation(summary = "Actualización de Datos", description = "Modificar los datos de un usuario ya registrado")
    public ResponseEntity<UsuarioResponse>actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request){

        UsuarioResponse usuarioResponse = usuarioService.actualizarUsuario(id,request);

        return ResponseEntity.ok(usuarioResponse);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Usuario", description = "Eliminación de un usuario registrado a través de su id ")
    public ResponseEntity<Void>eliminar(@PathVariable Long id){

        usuarioService.eliminarPorId(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}/admin")
    @Operation(summary = "Asignar rol Admin", description = "Asignar el rol admin a un usuario registrado como cliente")
    public ResponseEntity<UsuarioResponse>convertirUsuarioAdmin(@PathVariable Long id){

        UsuarioResponse usuarioResponse = usuarioService.asignarRolAdminPorIdUsuario(id);

        return ResponseEntity.ok(usuarioResponse);

    }


}
