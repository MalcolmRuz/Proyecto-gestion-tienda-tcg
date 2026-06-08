package com.gestion.tienda.tcg.registro.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import com.gestion.tienda.tcg.registro.enums.TipoDespacho;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

//Define esta clase como Entidad asociada a la tabla usuarios
@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class Usuario implements UserDetails{ // Se implementa Interfaz UserDetails,
                                             // que reconoce a la clase "Usuario", como
                                             // un usuario válido para Spring Security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, length = 40)
    private String nombre;

    @Column(nullable = false, length = 40)
    private String email;

    // Contraseña encriptada
    @Column(nullable = false, length = 250)
    private String password;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    private String direccion;

    //Tipo de despacho selecionado de enums/TipoDespacho
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoDespacho tipoDespacho;

    //Relacion de muchos usuarios pueden solo tener un rol
    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol rol;

    //Retorna los permisos/autorizaciones del usuario
    //Spring Security utiliza lo siguiente para validar roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + rol.getNombreRol().name()
                )
        );

    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}


