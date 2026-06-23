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
