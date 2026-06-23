**************************************************************************************  API - CARRITO   ***********************************************************************************************************************

[README-Carrito.md](https://github.com/user-attachments/files/29269370/README-Carrito.md)
```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**🛒 API - [Carrito]**

Gestiona el carrito de compras de los usuarios. Permite agregar o quitar productos, verificar stock en tiempo real y procesar el pago, generando automáticamente el pedido al confirmar.

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
| Nombre en Eureka | `CARRITO` |
| Puerto | `8083` |
| Base de datos | MySQL (`carrito`) |
| Eureka | `http://localhost:8761/eureka/` |

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `INVENTARIO-SERVICE` | Consultar stock disponible y descontar unidades al pagar |
| `PRODUCTO-SERVICE` | Obtener precio y descripción del producto al agregar un item |
| `PEDIDO-SERVICE` | Crear el pedido automáticamente tras confirmar el pago |

La comunicación entre servicios se realiza mediante **Feign** con propagación del token JWT.

---

## Autenticación

Todos los endpoints requieren token JWT en el header:
```
Authorization: Bearer <token>
```

---

## Estados del carrito

```
ACTIVO ──► PAGADO
ACTIVO ──► CANCELADO
ACTIVO ──► PENDIENTE   (stock insuficiente al intentar pagar)
PENDIENTE ──► ACTIVO   (reactivación manual)
PENDIENTE ──► CANCELADO
```

| Estado | Descripción |
|---|---|
| `ACTIVO` | Operativo, se pueden agregar y quitar items |
| `PENDIENTE` | Pago fallido por stock insuficiente, esperando reactivación |
| `PAGADO` | Pago confirmado, no se puede modificar |
| `CANCELADO` | Anulado manualmente |

---

## Endpoints

Base URL: `http://localhost:8083/api/v1`

### Carritos

| Método | Endpoint | Descripción | Body |
|---|---|---|---|
| `POST` | `/carritos` | Crear un nuevo carrito | `{}` |
| `GET` | `/carritos` | Listar todos los carritos | — |
| `GET` | `/carritos/{id}` | Obtener carrito por ID | — |
| `POST` | `/carritos/{id}/pagar` | Pagar el carrito y generar pedido | `PagarCarritoRequest` |
| `POST` | `/carritos/{id}/cancelar` | Cancelar el carrito | — |
| `POST` | `/carritos/{id}/reactivar` | Reactivar un carrito en estado PENDIENTE | — |
| `DELETE` | `/carritos/{id}` | Eliminar el carrito físicamente | — |

#### Body: `PagarCarritoRequest`
```json
{
  "usuarioId": 1,
  "direccionEnvio": "Av. Siempre Viva 742"
}
```

---

### Items del carrito

| Método | Endpoint | Descripción | Body |
|---|---|---|---|
| `POST` | `/carritos/{idCarrito}/items` | Agregar un producto al carrito | `CarritoItemRequest` |
| `GET` | `/carritos/{idCarrito}/items` | Listar todos los items del carrito | — |
| `GET` | `/carritos/{idCarrito}/items/{idItem}` | Ver un item específico | — |
| `PATCH` | `/carritos/items/{idItem}` | Actualizar cantidad de un item | `ActualizarCantidadRequest` |
| `DELETE` | `/carritos/items/{idItem}` | Eliminar un item del carrito | — |

#### Body: `CarritoItemRequest`
```json
{
  "productoId": 5,
  "cantidad": 2
}
```

#### Body: `ActualizarCantidadRequest`
```json
{
  "cantidad": 3
}
```

---

### Historial del carrito

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/carritos/historial/{idCarrito}` | Ver el historial de cambios de un carrito |

---

## Flujo de pago

1. El cliente llama a `POST /carritos/{id}/pagar` con `usuarioId` y `direccionEnvio`.
2. Se valida que el carrito esté `ACTIVO` y tenga items.
3. Se consulta el stock de cada item en `INVENTARIO-SERVICE`.
   - Si algún item no tiene stock suficiente → carrito pasa a `PENDIENTE` y se lanza error.
4. Se descuenta el stock de todos los items.
5. El carrito pasa a estado `PAGADO`.
6. Se crea automáticamente el pedido en `PEDIDO-SERVICE`.
   - Si la creación del pedido falla, el carrito igual queda `PAGADO` y se registra una advertencia en el historial.

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8083/swagger-ui.html
```


**************************************************************************************  API - PAGOS   ***********************************************************************************************************************


[README-Pagos.md](https://github.com/user-attachments/files/29269379/README-Pagos.md)
```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**💳 API - [Pagos]**

Procesa y registra los pagos asociados a pedidos. Valida el monto, aplica reglas de negocio (vuelto, rechazo por monto insuficiente) y actualiza el estado del pedido en consecuencia.

---

## Tecnologías

- Java 17
- Spring Boot (Web, Data JPA)
- Spring Cloud OpenFeign
- Oracle SQL
- Spring Cloud Eureka Client

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `pago` |
| Puerto | `8081` |
| Eureka | `http://localhost:8761/eureka/` |

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `PEDIDO-SERVICE` | Obtiene el total del pedido y actualiza su estado tras el pago |

La comunicación se realiza mediante **Feign** con propagación del token JWT.

---

## Endpoints

Base URL: `http://localhost:8081/api/v1`

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/pagos` | Realizar un pago asociado a un pedido |
| `GET` | `/pagos` | Listar todos los pagos registrados |

#### Body: realizar un pago
```json
{
  "idPedido": 3,
  "monto": 25000
}
```

#### Lógica del pago
- Si el pedido ya tiene un pago `APROBADO` → se rechaza (no se puede pagar dos veces).
- Si el monto es **menor** al total del pedido → pago `RECHAZADO`, pedido vuelve a `PENDIENTE_PAGO`.
- Si el monto es **igual o mayor** al total → pago `APROBADO`, pedido pasa a `PAGADO`.
- Si hay exceso de monto → se calcula y registra el vuelto.

---

## Estados del pago

| Estado | Descripción |
|---|---|
| `PENDIENTE` | Pago creado pero aún no procesado |
| `APROBADO` | Pago exitoso |
| `RECHAZADO` | Monto insuficiente |
| `CANCELADO` | Anulado manualmente |

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8081/swagger-ui.html
```

**************************************************************************************  API - REGISTRO   ***********************************************************************************************************************


[README-Registro.md](https://github.com/user-attachments/files/29269394/README-Registro.md)
```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**👤 API - [Registro]**

Gestiona el registro, autenticación y administración de usuarios del sistema. Genera tokens JWT necesarios para acceder al resto de los microservicios.

---

## Tecnologías

- Java 17
- Spring Boot (Web, Security, Data JPA)
- Oracle SQL
- JWT
- Spring Cloud Eureka Client

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `registro` |
| Puerto | `8082` |
| Eureka | `http://localhost:8761/eureka/` |

---

## Endpoints

Base URL: `http://localhost:8082/api/v1`

### Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/usuarios/auth/login` | Iniciar sesión y obtener token JWT |

#### Body: login
```json
{
  "email": "usuario@tienda.cl",
  "password": "miContraseña123"
}
```

#### Respuesta: token JWT
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Usa este token en los demás microservicios como header:
```
Authorization: Bearer <token>
```

---

### Usuarios

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/usuarios` | Registrar un nuevo usuario (rol CLIENTE por defecto) |
| `GET` | `/usuarios` | Listar todos los usuarios |
| `GET` | `/usuarios/{id}` | Buscar un usuario por ID |
| `PUT` | `/usuarios/{id}/actualizar` | Actualizar datos de un usuario |
| `DELETE` | `/usuarios/{id}` | Eliminar un usuario |
| `PUT` | `/usuarios/{id}/admin` | Asignar rol ADMIN a un usuario |

#### Body: registrar usuario
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@tienda.cl",
  "password": "segura123",
  "tipoDespacho": "DOMICILIO",
  "direccion": "Av. Siempre Viva 742"
}
```

> Si `tipoDespacho` es `DOMICILIO`, el campo `direccion` es obligatorio.

---

## Roles

| Rol | Descripción |
|---|---|
| `CLIENTE` | Rol por defecto al registrarse |
| `ADMIN` | Se asigna manualmente con `PUT /usuarios/{id}/admin` |

## Reglas de negocio

- Todo usuario nuevo se crea con rol `CLIENTE`.
- Un usuario solo puede tener un rol a la vez.
- No se puede asignar `ADMIN` si el usuario ya tiene ese rol.
- El tipo de despacho `DOMICILIO` exige dirección obligatoria.

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8082/swagger-ui.html
```


**************************************************************************************  API - INVENTARIO   ***********************************************************************************************************************

[README-Inventario.md](https://github.com/user-attachments/files/29269395/README-Inventario.md)
```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**📊 API - [Inventario]**

Gestiona el stock de productos y lleva un historial de todos los movimientos de entrada y salida. Normalmente el inventario se crea de forma automática cuando se registra un producto, no es necesario crearlo manualmente.

---

## Tecnologías

- Java 17
- Spring Boot (Web, Data JPA)
- Oracle SQL
- Spring Cloud Eureka Client

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `INVENTARIO-SERVICE` |
| Puerto | `8085` |
| Eureka | `http://localhost:8761/eureka/` |

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `PRODUCTO-SERVICE` | Crea el inventario automáticamente al registrar un producto |
| `CARRITO` | Descuenta stock al confirmar el pago del carrito |

---

## Endpoints

Base URL: `http://localhost:8085/api/v1`

### Inventario

| Método | Endpoint | Descripción | Requiere |
|---|---|---|---|
| `POST` | `/inventarios` | Crear un inventario manualmente (no recomendado, se crea automático desde Productos) | — |
| `GET` | `/inventarios/producto/{idProducto}` | Consultar el inventario y stock de un producto | Autenticación |
| `PATCH` | `/inventarios/{idProducto}/aumentar/{cantidad}` | Aumentar el stock de un producto | Rol ADMIN |
| `PATCH` | `/inventarios/{idProducto}/reducir/{cantidad}` | Reducir el stock de un producto | Autenticación |
| `PUT` | `/inventarios` | Corregir un inventario (solo usar si hay un error de datos) | Autenticación |

#### Ejemplo: aumentar stock
```
PATCH /api/v1/inventarios/5/aumentar/10
```

#### Ejemplo: reducir stock
```
PATCH /api/v1/inventarios/5/reducir/2
```

---

### Movimientos de Stock

Cada cambio de stock queda registrado automáticamente en el historial.

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/movimientosStock` | Listar todos los movimientos de stock |
| `GET` | `/movimientosStock/reportePorfecha` | Filtrar movimientos entre dos fechas |

#### Ejemplo: filtrar movimientos por fecha
```
GET /api/v1/movimientosStock/reportePorfecha?fechaInicio=2024-01-01&fechaFin=2024-01-31
```

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8085/swagger-ui.html
```


**************************************************************************************  API - PRODUCTO   ***********************************************************************************************************************


[README-Producto.md](https://github.com/user-attachments/files/29269399/README-Producto.md)
```
   ____     ____      _   _   ____    U  ___ u             U  ___ u  __  __     ____   
U /"___|uU |  _"\ uU |"|u| |U|  _"\ u  \/"_ \/              \/"_ \/U|' \/ '|uU /"___|u 
\| |  _ / \| |_) |/ \| |\| |\| |_) |/  | | | |     U  u     | | | |\| |\/| |/\| |  _ / 
 | |_| |   |  _ <    | |_| | |  __/.-,_| |_| |     /___\.-,_| |_| | | |  | |  | |_| |  
  \____|   |_| \_\  <<\___/  |_|    \_)-\___/     |__"__|\_)-\___/  |_|  |_|   \____|  
  _)(|_    //   \\_(__) )(   ||>>_       \\                   \\   <<,-,,-.    _)(|_   
 (__)__)  (__)  (__)   (__) (__)__)     (__)                 (__)   (./  \.)  (__)__)  
```
**📦 API - [Producto]**

Gestiona el catálogo de productos, categorías y proveedores de la tienda TCG. Al registrar un producto, se crea automáticamente un inventario en el microservicio de inventario.

---

## Tecnologías

- Java 17
- Spring Boot (Web, Data JPA)
- Oracle SQL
- Spring Cloud Eureka Client

---

## Configuración

| Propiedad | Valor |
|---|---|
| Nombre en Eureka | `PRODUCTO-SERVICE` |
| Puerto | `8086` |
| Eureka | `http://localhost:8761/eureka/` |

Asegúrate de tener estas propiedades en tu `application.properties` antes de iniciar el servicio.

---

## Dependencias con otros microservicios

| Microservicio | Uso |
|---|---|
| `INVENTARIO-SERVICE` | Se crea un inventario automáticamente al registrar un nuevo producto |

---

## ⚠️ Orden de registro importante

> Antes de crear un producto, **primero debes registrar un proveedor** en la base de datos.

---

## Endpoints

Base URL: `http://localhost:8086/api/v1`

### Productos

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/productos` | Crear un nuevo producto (también crea su inventario automáticamente) |
| `PUT` | `/productos/{id}` | Actualizar datos de un producto existente |
| `PATCH` | `/productos/{id}/desactivar` | Desactivar un producto (no se elimina, se conserva el historial) |
| `GET` | `/productos/proveedor/{idProveedor}` | Listar todos los productos de un proveedor |

#### Body: crear o actualizar producto
```json
{
  "nombre": "Mazo Pokémon",
  "precio": 15000,
  "idProveedor": 1
}
```

---

### Categorías

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/categoria` | Crear una nueva categoría (ej: "Singles", "Cajas", "Sobres") |
| `GET` | `/categoria` | Listar todas las categorías |
| `PUT` | `/categoria/{id}` | Editar una categoría existente |

---

### Relación Producto–Categoría

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/productoCategoria/asignar` | Asociar un producto con una categoría |
| `DELETE` | `/productoCategoria/quitar/{idRelacion}` | Quitar la asociación entre un producto y su categoría |
| `GET` | `/productoCategoria/categoria/{idCategoria}` | Listar todos los productos de una categoría |

#### Body: asignar categoría a producto
```json
{
  "idProducto": 3,
  "idCategoria": 2
}
```

---

### Proveedores

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/proveedor` | Registrar un nuevo proveedor |
| `GET` | `/proveedor` | Listar todos los proveedores |
| `PATCH` | `/proveedor/{id}/contacto?nuevoContacto=...` | Actualizar el dato de contacto de un proveedor |

#### Body: crear proveedor
```json
{
  "nombre": "Distribuidora TCG Chile",
  "contacto": "contacto@tcgchile.cl"
}
```

---

## Swagger UI

Con el servicio corriendo, puedes explorar y probar los endpoints en:

```
http://localhost:8086/swagger-ui.html
```


**************************************************************************************  API - PEDIDO   ***********************************************************************************************************************


[README-Pedido.md](https://github.com/user-attachments/files/29269401/README-Pedido.md)
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


*****************************************************************************************************************************************************************************************************************
