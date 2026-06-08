# Microservicio Pedido

Microservicio encargado de gestionar los pedidos del sistema de gestión de tienda TCG. Recibe los datos del carrito pagado para construir el pedido, administra su ciclo de vida mediante estados, y coordina la gestión del envío y el historial de cambios.

---

## Tecnologías

- Java 17
- Spring Boot 3.5.14
- Spring Data JPA
- Spring Security + JWT
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka Client
- MySQL
- Lombok
- Maven

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `PEDIDO-SERVICE` |
| Puerto | `8084` |
| Base de datos | MySQL (`pedido`) |

El servicio se registra automáticamente en Eureka al iniciar. Requiere que el servidor Eureka esté corriendo en `http://localhost:8761`.

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `CARRITO` | Obtener los items y datos del carrito pagado para construir el pedido |

La comunicación se realiza mediante **Feign** con propagación del token JWT a través de `FeignInterceptor`.

---

## Estados del pedido

```
PAGADO ──► PROCESANDO
PAGADO ──► CANCELADO
PROCESANDO ──► FINALIZADO
PROCESANDO ──► CANCELADO
FINALIZADO ──► (terminal, no cambia)
CANCELADO  ──► (terminal, no cambia)
```

| Estado | Descripción |
|---|---|
| `PAGADO` | Pedido creado tras confirmar el pago del carrito |
| `PROCESANDO` | Pedido en preparación |
| `FINALIZADO` | Pedido listo, en camino al cliente |
| `CANCELADO` | Pedido anulado |
| `PENDIENTE` | Estado inicial por defecto (pre-pago) |
| `PENDIENTE_PAGO` | A la espera de confirmación de pago |

---

## Estados del envío

Los estados del envío se actualizan automáticamente al cambiar el estado del pedido.

| Estado envío | Se asigna cuando el pedido pasa a |
|---|---|
| `PREPARANDO` | `PROCESANDO` |
| `EN_CAMINO` | `FINALIZADO` |
| `ENTREGADO` | Se marca manualmente vía endpoint `/entregado` |
| `CANCELADO` | `CANCELADO` |

---

## Endpoints

Todos los endpoints requieren autenticación mediante token JWT en el header `Authorization: Bearer <token>`.

Base URL: `http://localhost:8084/api/v1`

### Pedidos

| Método | Endpoint | Descripción | Body | Respuesta |
|---|---|---|---|---|
| `POST` | `/pedidos/{idCarrito}` | Crear pedido desde un carrito pagado | `CrearPedidoRequest` | `201 PedidoResponse` |
| `GET` | `/pedidos` | Listar todos los pedidos | — | `200 List<PedidoResponse>` |
| `GET` | `/pedidos/{id}` | Obtener pedido por ID | — | `200 PedidoResponse` |
| `GET` | `/pedidos/usuario/{usuarioId}` | Listar pedidos de un usuario | — | `200 List<PedidoResponse>` |
| `PATCH` | `/pedidos/{id}/estado` | Actualizar el estado del pedido | `?estado=PROCESANDO` | `200 PedidoResponse` |
| `PATCH` | `/pedidos/{id}/entregado` | Marcar el pedido como entregado | — | `200 PedidoResponse` |

#### Body: `CrearPedidoRequest`
```json
{
  "usuarioId": 1,
  "envio": {
    "direccionEnvio": "Av. Siempre Viva 742"
  }
}
```

> Este endpoint es invocado automáticamente por el microservicio `carrito` al confirmar el pago. No requiere ser llamado manualmente en el flujo normal.

#### Query param: `actualizarEstado`
```
PATCH /api/v1/pedidos/3/estado?estado=PROCESANDO
```
Valores válidos: `PROCESANDO`, `FINALIZADO`, `CANCELADO`

---

### Detalles del pedido

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/detalles/pedido/{idPedido}` | Listar los productos de un pedido | `200 List<DetallePedidoResponse>` |

---

### Envío

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/envios/pedido/{idPedido}` | Obtener el envío de un pedido | `200 EnvioResponse` |
| `PATCH` | `/envios/pedido/{idPedido}` | Actualizar el estado del envío manualmente | `200 EnvioResponse` |

#### Query param: `actualizarEstado envío`
```
PATCH /api/v1/envios/pedido/3?estado=ENTREGADO
```
Valores válidos: `PREPARANDO`, `EN_CAMINO`, `ENTREGADO`, `CANCELADO`

---

### Historial del pedido

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/historial/pedido/{idPedido}` | Listar el historial de estados de un pedido | `200 List<HistorialPedidoResponse>` |

---

## Flujo principal: creación de un pedido

1. El microservicio `carrito` llama a `POST /pedidos/{idCarrito}` tras confirmar el pago.
2. Se consulta el carrito en `CARRITO` mediante Feign y se valida que esté en estado `PAGADO`.
3. Se calcula el total sumando `precioUnitario × cantidad` de cada item.
4. Se persiste el pedido con estado `PAGADO` y el total ya calculado.
5. Se crean los registros de `DetallePedido` asociados al pedido.
6. Se crea el `Envio` con la dirección recibida.
7. Se registra el primer evento en el `HistorialPedido`.

---

## Estructura del proyecto

```
pedido/
└── src/main/java/com/gestion/tienda/tcg/pedido/
    ├── PedidoApplication.java
    ├── Client/
    │   └── CarritoClient.java
    ├── config/
    │   └── FeignInterceptor.java
    ├── controller/
    │   ├── PedidoController.java
    │   ├── DetallePedidoController.java
    │   ├── EnvioController.java
    │   └── HistorialPedidoController.java
    ├── dto/
    ├── enums/
    ├── exception/
    ├── mapper/
    ├── model/
    │   ├── Pedido.java
    │   ├── DetallePedido.java
    │   ├── Envio.java
    │   └── HistorialPedido.java
    ├── repository/
    ├── security/
    └── service/
        ├── PedidoService.java
        ├── DetallePedidoService.java
        ├── EnvioService.java
        └── HistorialPedidoService.java
```
