# Docker Setup para TCG Tienda

## Descripción
Este setup Docker contiene dos microservicios Spring Boot con sus propias bases de datos MySQL.

### Estructura
- **carrito**: Microservicio de carrito (puerto 8083)
- **pedido**: Microservicio de pedidos (puerto 8084)
- **carrito-db**: Base de datos MySQL para carrito (puerto 3307)
- **pedido-db**: Base de datos MySQL para pedido (puerto 3308)

## Requisitos
- Docker
- Docker Compose

## Cómo ejecutar

### Opción 1: Construir y ejecutar todo
```bash
docker-compose up --build
```

### Opción 2: Construir en background
```bash
docker-compose up -d --build
```

### Ver logs
```bash
# Todos los servicios
docker-compose logs -f

# Solo carrito
docker-compose logs -f carrito

# Solo pedido
docker-compose logs -f pedido
```

### Detener servicios
```bash
docker-compose down
```

### Detener y limpiar volúmenes (elimina datos)
```bash
docker-compose down -v
```

## Acceso a los servicios

### Carrito
- Aplicación: http://localhost:8083
- Swagger: http://localhost:8083/swagger-ui.html

### Pedido
- Aplicación: http://localhost:8084
- Swagger: http://localhost:8084/swagger-ui.html

## Acceso a las bases de datos

### Carrito MySQL
```
Host: localhost
Puerto: 3307
Usuario: carrito_user
Contraseña: carrito_pass
Base de datos: carrito
```

### Pedido MySQL
```
Host: localhost
Puerto: 3308
Usuario: pedido_user
Contraseña: pedido_pass
Base de datos: pedido
```

## Variables de entorno

Las variables de entorno están definidas en `docker-compose.yml`. Para cambiar valores, edita el archivo directamente o utiliza un archivo `.env`.

## Configuración de la red

Ambos servicios están conectados a una red Docker compartida (`tcg-network`), permitiendo comunicación interna:
- carrito puede alcanzar: http://pedido:8084
- pedido puede alcanzar: http://carrito:8083

## Notas importantes

1. **Base de datos**: Cada microservicio tiene su propia instancia MySQL independiente.
2. **Compilación**: Los Dockerfiles usan multi-stage build para compilar y empaquetar dentro de contenedores.
3. **Persistencia**: Los datos de la base de datos se almacenan en volúmenes Docker (`carrito_db_data` y `pedido_db_data`).
4. **Health Check**: Las bases de datos incluyen health checks para asegurar disponibilidad antes de iniciar los servicios.
5. **Eureka**: Si tienes Eureka en Docker, descomenta las líneas `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` en el docker-compose.yml

## Solución de problemas

### Puerto ya está en uso
```bash
# Cambiar puerto en docker-compose.yml
# Por ejemplo, cambiar 8083:8083 a 8086:8083
```

### Contenedores no inician
```bash
# Ver logs
docker-compose logs

# Rebuilds
docker-compose build --no-cache
```

### Limpiar todo (cuidado!)
```bash
docker-compose down -v
docker system prune -a
```

## Próximos pasos

Para agregar Eureka:
1. Agregar servicio eureka en docker-compose.yml
2. Descomenta la línea EUREKA en los servicios
3. Ejecuta `docker-compose up --build`
