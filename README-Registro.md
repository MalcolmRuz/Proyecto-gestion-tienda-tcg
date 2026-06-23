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
