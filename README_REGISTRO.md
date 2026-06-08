
API - [USUARIOS]

Este módulo gestiona el registro, autenticación y administración de usuarios del sistema.

##  Tecnologías
* Java 17
* Spring Boot (Web, Security, Data JPA)
* Oracle SQL
* JWT

---

## Endpoints principales

POST /api/v1/usuarios  
→ Registra usuario (rol CLIENTE por defecto, validación de dirección si es DOMICILIO)

POST /api/v1/usuarios/auth/login  
→ Autentica usuario y genera JWT

GET /api/v1/usuarios/{id}  
→ Busca usuario por ID

GET /api/v1/usuarios  
→ Lista todos los usuarios

PUT /api/v1/usuarios/{id}/actualizar  
→ Actualiza datos del usuario

DELETE /api/v1/usuarios/{id}  
→ Elimina usuario

PUT /api/v1/usuarios/{id}/admin  
→ Asigna rol ADMIN (si no lo tiene)

---

## Reglas de negocio

- Todo usuario se crea como CLIENTE
- Un usuario solo puede tener un rol
- Login requiere credenciales válidas
- DOMICILIO exige dirección obligatoria
- No se puede reasignar ADMIN si ya lo tiene
