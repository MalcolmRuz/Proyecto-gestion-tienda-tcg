```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**📋 API - [Pedido]**

Gestiona el ciclo de vida de los pedidos. Recibe los datos del carrito pagado para construir el pedido, administra sus estados y coordina el seguimiento del envío.

> Este microservicio es invocado automáticamente por el microservicio `CARRITO` al confirmar el pago. En el flujo normal, no necesitas llamarlo directamente para crear pedidos.

---

## Tecnologías

- Java 17
- Spring Boot 3.5.14 (Web, Data JPA, Security)
- JWT
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
| Eureka | `http://localhost:8761/eureka/` |

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `CARRITO` | Obtener los items y datos del carrito pagado para construir el pedido |

La comunicación se realiza mediante **Feign** con propagación del token JWT.

---

## Autenticación

Todos los endpoints requieren token JWT en el header:
```
Authorization: Bearer <token>
```

---

## Estados del pedido

```
PAGADO ──► PROCESANDO
PAGADO ──► CANCELADO
PROCESANDO ──► FINALIZADO
PROCESANDO ──► CANCELADO
FINALIZADO ──► (estado final)
CANCELADO  ──► (estado final)
```

| Estado | Descripción |
|---|---|
| `PAGADO` | Pedido creado tras confirmar el pago del carrito |
| `PROCESANDO` | Pedido en preparación |
| `FINALIZADO` | Pedido listo, en camino al cliente |
| `CANCELADO` | Pedido anulado |

---

## Estados del envío

El estado del envío se actualiza automáticamente al cambiar el estado del pedido.

| Estado envío | Cuándo se asigna |
|---|---|
| `PREPARANDO` | Pedido pasa a `PROCESANDO` |
| `EN_CAMINO` | Pedido pasa a `FINALIZADO` |
| `ENTREGADO` | Se marca manualmente vía `/envios/pedido/{id}?estado=ENTREGADO` |
| `CANCELADO` | Pedido pasa a `CANCELADO` |

---

## Endpoints

Base URL: `http://localhost:8084/api/v1`

### Pedidos

| Método | Endpoint | Descripción | Body |
|---|---|---|---|
| `POST` | `/pedidos/{idCarrito}` | Crear pedido desde un carrito pagado (uso automático) | `CrearPedidoRequest` |
| `GET` | `/pedidos` | Listar todos los pedidos | — |
| `GET` | `/pedidos/{id}` | Obtener pedido por ID | — |
| `GET` | `/pedidos/usuario/{usuarioId}` | Listar pedidos de un usuario | — |
| `PATCH` | `/pedidos/{id}/estado?estado=PROCESANDO` | Actualizar estado del pedido | — |
| `PATCH` | `/pedidos/{id}/entregado` | Marcar el pedido como entregado | — |

#### Body: `CrearPedidoRequest`
```json
{
  "usuarioId": 1,
  "envio": {
    "direccionEnvio": "Av. Siempre Viva 742"
  }
}
```

#### Valores válidos para actualizar estado
```
PATCH /api/v1/pedidos/3/estado?estado=PROCESANDO
```
Valores: `PROCESANDO`, `FINALIZADO`, `CANCELADO`

---

### Detalles del pedido

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/detalles/pedido/{idPedido}` | Listar los productos incluidos en un pedido |

---

### Envío

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/envios/pedido/{idPedido}` | Ver el envío asociado a un pedido |
| `PATCH` | `/envios/pedido/{idPedido}?estado=ENTREGADO` | Actualizar estado del envío manualmente |

Valores válidos para el estado del envío: `PREPARANDO`, `EN_CAMINO`, `ENTREGADO`, `CANCELADO`

---

### Historial del pedido

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/historial/pedido/{idPedido}` | Ver el historial de cambios de estado de un pedido |

---

## Flujo de creación de un pedido

1. El microservicio `CARRITO` llama a `POST /pedidos/{idCarrito}` tras confirmar el pago.
2. Se consulta el carrito via Feign y se valida que esté en estado `PAGADO`.
3. Se calcula el total sumando `precioUnitario × cantidad` de cada item.
4. Se persiste el pedido con estado `PAGADO`.
5. Se crean los registros de `DetallePedido`.
6. Se crea el `Envio` con la dirección recibida.
7. Se registra el primer evento en el `HistorialPedido`.

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8084/swagger-ui.html
```
