package com.gestion.tienda.tcg.registro.config;

import com.gestion.tienda.tcg.registro.enums.NombreRol;
import com.gestion.tienda.tcg.registro.enums.TipoDespacho;
import com.gestion.tienda.tcg.registro.model.Rol;
import com.gestion.tienda.tcg.registro.model.Usuario;
import com.gestion.tienda.tcg.registro.repository.RolRepository;
import com.gestion.tienda.tcg.registro.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

// Configuración encargada de inicializar
// datos básicos al iniciar la aplicación
@Configuration
public class DataInitializer {

    // Inserta roles y crea un usuario administrador
    // por defecto al arrancar el sistema
    @Bean
    @Transactional
    CommandLineRunner initData(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) { // Inicializa datos al arrancar el sistema
        return args -> {

            // Crea un rol ADMIN si no existe

            if (rolRepository.findByNombreRol(NombreRol.ADMIN).isEmpty()) {
                rolRepository.save(new Rol(null, NombreRol.ADMIN));
            }

            // Crea un rol CLIENTE si no existe

            if (rolRepository.findByNombreRol(NombreRol.CLIENTE).isEmpty()) {
                rolRepository.save(new Rol(null, NombreRol.CLIENTE));

            }

            // Crea un usuario admin por defecto

            if (usuarioRepository.findByEmail("admin@gmail.com").isEmpty()) {

                // Obtener rol ADMIN desde la base de datos
                Rol rolAdmin = rolRepository.findByNombreRol(NombreRol.ADMIN)
                        .orElseThrow();

                Usuario admin = new Usuario();

                admin.setNombre("Administrador");
                admin.setEmail("admin@gmail.com");

                // Encripta la contraseña antes de guardarla
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFechaCreacion(LocalDate.now());
                admin.setTipoDespacho(TipoDespacho.TIENDA);
                admin.setRol(rolAdmin);

                //Se guarda el usuario como ADMIN
                usuarioRepository.save(admin);

            }
        };
    }
}
