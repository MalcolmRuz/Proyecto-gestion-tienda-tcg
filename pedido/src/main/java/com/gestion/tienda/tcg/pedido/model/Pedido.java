package com.gestion.tienda.tcg.pedido.model;

import java.util.List;

import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private Long idCarrito;
    private Double totalPedido;
    private String direccionEnvio;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estadoEnvio; // Mantenemos el enum para trazar el estado

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<HistorialPedido> historial;
}