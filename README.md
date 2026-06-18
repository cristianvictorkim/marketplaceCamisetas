# Marketplace Camisetas API

API REST para el marketplace de camisetas del Mundial 2026, desarrollada con
Spring Boot, Spring Security, JPA y PostgreSQL.

## Requisitos

- Java 17
- PowerShell
- Acceso a la base compartida de Neon
- Docker Desktop únicamente si se quiere usar PostgreSQL local

El proyecto incluye su propia instalación de Maven mediante `mvnw.cmd`.

## Base de datos compartida

El entorno de trabajo del equipo utiliza PostgreSQL 16 alojado en Neon. Los
usuarios, productos, carritos, stocks y pedidos se almacenan en esa misma base,
por lo que no es necesario que la computadora de otro integrante permanezca
encendida.

### Configuración inicial

La rama `conexion-bd` incluye el archivo `.env` con la conexión compartida de
Neon. Al clonar el repositorio o cambiar a esta rama, el backend recibe
automáticamente la misma configuración:

```env
DB_URL=jdbc:postgresql://HOST-POOLER-DE-NEON/neondb?sslmode=require&channelBinding=require
DB_USERNAME=neondb_owner
DB_PASSWORD=CONTRASENA_DE_NEON

JWT_SECRET=SECRETO_COMPARTIDO_POR_EL_EQUIPO
JWT_EXPIRATION_MS=86400000
CATALOG_SEEDER_ENABLED=false
MIGRATE_H2_TO_POSTGRES=false
JPA_SHOW_SQL=false
```

No es necesario crear ni editar `.env` para usar la base compartida. La URL que
entrega Neon comienza con `postgresql://`; Spring Boot la utiliza con el prefijo
`jdbc:postgresql://`.

Esta decisión expone las credenciales a toda persona con acceso al repositorio.
Si la contraseña cambia, el archivo `.env` versionado debe actualizarse y todo
el equipo debe descargar el nuevo commit.

## Ejecutar el backend

Desde la carpeta `marketplace`:

```powershell
.\scripts\start-backend.ps1
```

Servicios disponibles:

- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html

El script carga automáticamente las variables definidas en `.env`.

## Levantar la aplicación completa

1. En una terminal, desde `marketplace`:

```powershell
.\scripts\start-backend.ps1
```

2. En otra terminal, desde `EccomerceCamisetas-Front\client`:

```powershell
npm install
npm run dev
```

3. Abrir http://localhost:5173. El backend debe continuar ejecutándose en
   http://localhost:8080.

Para probar el panel administrativo:

- Email: `admin@mail.com`
- Contraseña: `Password123!`

## Estado de la migración

La base H2 original ya fue migrada a Neon. Se conservaron los IDs, relaciones,
usuarios, productos, variantes, stocks, descuentos, carritos y pedidos.

No se debe volver a ejecutar la migración sobre la base compartida existente.
La opción siguiente se conserva únicamente para migrar una H2 hacia una base
PostgreSQL nueva y vacía:

```powershell
.\scripts\start-backend.ps1 -MigrateH2
```

El proceso se detiene automáticamente si detecta registros en la base de
destino, evitando sobrescribir información.

## Alternativas locales

### PostgreSQL con Docker

Para trabajar con una PostgreSQL local:

```powershell
docker compose up -d
$env:DB_URL = "jdbc:postgresql://localhost:5432/marketplace"
$env:DB_USERNAME = "marketplace"
$env:DB_PASSWORD = "marketplace_dev"
.\mvnw.cmd -s .mvn\settings.xml "-Dmaven.compiler.fork=true" spring-boot:run
```

Configuración predeterminada:

- Base: `marketplace`
- Puerto: `5432`
- Usuario: `marketplace`
- Contraseña: `marketplace_dev`

Para detener el contenedor:

```powershell
docker compose down
```

### H2

H2 permanece disponible como respaldo local:

```powershell
.\scripts\start-backend.ps1 -Local
```

Consola H2: http://localhost:8080/h2-console

## Credenciales de administrador para pruebas

- Usuario: `admin@mail.com`
- Contraseña: `Password123!`

## Endpoints

### Autenticación

- `POST /api/auth/register`: registra un usuario cliente.
- `POST /api/auth/login`: inicia sesión y devuelve el token JWT.
- `POST /api/auth/bootstrap-admin`: crea el administrador inicial.

### Usuarios

- `GET /api/usuarios/me`: devuelve la cuenta autenticada.
- `PUT /api/usuarios/me`: actualiza sus datos.
- `PATCH /api/usuarios/me/password`: cambia su contraseña.
- `DELETE /api/usuarios/me`: elimina su cuenta.
- `GET /api/usuarios`: lista usuarios; requiere rol administrador.
- `GET /api/usuarios/{id}`: devuelve un usuario; requiere administrador.
- `POST /api/usuarios`: crea un usuario; requiere administrador.
- `PUT /api/usuarios/{id}`: actualiza un usuario; requiere administrador.
- `DELETE /api/usuarios/{id}`: elimina un usuario; requiere administrador.

### Catálogo

Las consultas son públicas. Las operaciones de escritura requieren rol
administrador.

- `/api/catalogo/generos`: gestión de géneros.
- `/api/catalogo/talles`: gestión de talles.
- `/api/catalogo/tipos-camiseta`: gestión de tipos de camiseta.
- `/api/catalogo/paises`: gestión de selecciones y países.

### Camisetas

- `GET /api/camisetas`: lista las camisetas publicadas.
- `GET /api/camisetas/{id}`: devuelve el detalle de una camiseta.
- `POST /api/camisetas`: crea una camiseta; requiere administrador.
- `PUT /api/camisetas/{id}`: actualiza una camiseta; requiere administrador.
- `DELETE /api/camisetas/{id}`: elimina una camiseta; requiere administrador.
- `GET /api/camisetas/{id}/variantes`: lista talles y stock.
- `POST /api/camisetas/{id}/variantes`: crea una variante.
- `PUT /api/camisetas/variantes/{id}`: actualiza una variante.
- `DELETE /api/camisetas/variantes/{id}`: elimina una variante.
- `PATCH /api/camisetas/variantes/{id}/stock`: modifica el stock.
- `/api/camisetas/{id}/descuento`: gestión de descuentos.

### Carrito

- `GET /api/carrito`: devuelve el carrito actual.
- `POST /api/carrito/items`: agrega un producto.
- `PATCH /api/carrito/items/{id}`: modifica la cantidad.
- `DELETE /api/carrito/items/{id}`: elimina un producto.
- `DELETE /api/carrito`: vacía el carrito.

### Favoritos

Requieren un usuario autenticado con rol `USER`.

- `GET /api/favoritos`: lista las camisetas favoritas activas del usuario.
- `POST /api/favoritos/{camisetaId}`: agrega una camiseta a favoritos.
- `DELETE /api/favoritos/{camisetaId}`: elimina una camiseta de favoritos.
- `GET /api/favoritos/{camisetaId}/existe`: indica si la camiseta es favorita.

Agregar o eliminar es idempotente: repetir la misma operación no genera
duplicados ni errores.

### Pedidos

- `POST /api/pedidos`: genera un pedido a partir del carrito.
- `GET /api/pedidos`: devuelve el historial de pedidos.
- `GET /api/pedidos/{id}`: devuelve el detalle de un pedido.
- `PATCH /api/pedidos/{id}/cancelar`: cancela un pedido.
- `PATCH /api/pedidos/{id}/estado`: cambia su estado; requiere administrador.
