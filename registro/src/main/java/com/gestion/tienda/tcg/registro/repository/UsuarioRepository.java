package com.gestion.tienda.tcg.registro.repository;

import com.gestion.tienda.tcg.registro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repositorio encargado de acceder y manipular
// datos de la entidad Usuario

public interface UsuarioRepository extends JpaRepository <Usuario,Long> {

    //Busca un usuario mediante su correo electrónico.
    Optional<Usuario> findByEmail(String email);

}
