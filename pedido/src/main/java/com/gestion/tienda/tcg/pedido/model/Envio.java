package com.gestion.tienda.tcg.pedido.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "envio")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnvio;

    @Column(nullable = false)
    private String direccionEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estadoEnvio;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    // RELACION 1:1 CON PEDIDO
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference("pedido-envio")
    private Pedido pedido;

    @PrePersist
    public void prePersist() {

        fechaEnvio = LocalDateTime.now();

        if (estadoEnvio == null) {
            estadoEnvio = EstadoEnvio.PREPARANDO;
        }
    }
}