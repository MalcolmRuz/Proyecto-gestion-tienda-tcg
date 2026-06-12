package com.gestion_tienda_tcg.productos.controller.v2;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/categoria")
@Tag(name = "CATEGORIA V2", description = "Control de categorias optimizada con HATEOAS")
public class CategoriaControllerV2 {
}
