package com.gestion_tienda_tcg.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InventarioResponse {
    private final Long idInventario;
    private final Long idProducto;
    private final int stockActual;
    private final LocalDateTime fechaInventario;
}
