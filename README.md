# Marketplace Camisetas API

API REST del e-commerce de camisetas del Mundial 2026, desarrollada con Spring
Boot, Spring Security, JPA, JWT y PostgreSQL.

## Requisitos

- Java 8 o superior.
- PowerShell.
- Node.js 18 o superior y npm para ejecutar el frontend.
- Acceso a Internet en la primera ejecución para descargar dependencias.

El backend incluye Maven Wrapper, por lo que no es necesario instalar Maven.

## Levantar la aplicación completa

1. Desde la carpeta `marketplace`, iniciar el backend:

```powershell
.\scripts\start-backend.ps1
```

2. En otra terminal, desde `EccomerceCamisetas-Front\client`, iniciar el
   frontend:

```powershell
npm install
npm run dev
```

3. Abrir http://localhost:5173.

Servicios disponibles:

- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html
- Frontend: http://localhost:5173

La primera ejecución puede demorar mientras Maven y npm descargan las
dependencias.

## Base de datos y archivo `.env`

El archivo `.env` contiene la configuración privada y no se versiona. Para
preparar el entorno, copiar `.env.example` como `.env` y completar sus valores.
El script `start-backend.ps1` carga automáticamente esas variables.

Variables utilizadas:

```env
DB_URL=jdbc:postgresql://HOST/neondb
DB_USERNAME=USUARIO
DB_PASSWORD=CONTRASEÑA
JWT_SECRET=SECRETO
JWT_EXPIRATION_MS=86400000
CATALOG_SEEDER_ENABLED=false
MIGRATE_H2_TO_POSTGRES=false
JPA_SHOW_SQL=false
JPA_DDL_AUTO=update
BOOTSTRAP_ADMIN_ENABLED=false
CORS_ALLOWED_ORIGIN_PATTERNS=http://localhost:5173
LOGGING_LEVEL_ROOT=INFO
```

El bootstrap de administrador permanece desactivado por defecto. Solo debe
habilitarse temporalmente en una instalación nueva sin administradores.

Para un frontend desplegado, `CORS_ALLOWED_ORIGIN_PATTERNS` debe contener sus
orígenes permitidos separados por coma.

## Credenciales de administrador

- Email: `admin@uade.edu.ar`
- Contraseña: `123456`

## Funcionalidades

- Registro, login y autorización por roles mediante JWT.
- Gestión de usuarios y perfil.
- Catálogo público con búsqueda y filtros.
- CRUD administrativo de camisetas, variantes, stock y descuentos.
- Favoritos persistidos por usuario.
- Carrito persistido y validado en backend.
- Creación e historial de pedidos.
- Descuento de stock durante el checkout.
- Reposición de stock al cancelar.
- Bloqueo de cambios de estado para pedidos cancelados.
- Catálogos de países, géneros, talles y tipos de camiseta.
- Documentación interactiva con Swagger.

## Endpoints principales

### Autenticación

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/bootstrap-admin` (desactivado por defecto)

### Usuarios

- `GET /api/usuarios/me`
- `PUT /api/usuarios/me`
- `PATCH /api/usuarios/me/password`
- `DELETE /api/usuarios/me`
- `/api/usuarios`: operaciones administrativas.

### Camisetas y catálogos

- `GET /api/camisetas`
- `GET /api/camisetas/{id}`
- `POST /api/camisetas`: crea la camiseta y todas sus variantes en una única
  transacción y devuelve el producto completo.
- `PUT /api/camisetas/{id}`: sincroniza producto y variantes en una única
  transacción y devuelve el producto actualizado.
- `/api/camisetas/**`: gestión administrativa.
- `/api/catalogo/generos`
- `/api/catalogo/talles`
- `/api/catalogo/tipos-camiseta`
- `/api/catalogo/paises`

### Carrito

- `GET /api/carrito`
- `POST /api/carrito/items`
- `PATCH /api/carrito/items/{id}`
- `DELETE /api/carrito/items/{id}`
- `DELETE /api/carrito`

### Favoritos

- `GET /api/favoritos`
- `POST /api/favoritos/{camisetaId}`
- `DELETE /api/favoritos/{camisetaId}`
- `GET /api/favoritos/{camisetaId}/existe`

### Pedidos

- `POST /api/pedidos`
- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `PATCH /api/pedidos/{id}/cancelar`
- `PATCH /api/pedidos/{id}/estado`

## Alternativas locales

### H2

```powershell
.\scripts\start-backend.ps1 -Local
```

Consola: http://localhost:8080/h2-console

### PostgreSQL con Docker

```powershell
docker compose up -d
$env:DB_URL = "jdbc:postgresql://localhost:5432/marketplace"
$env:DB_USERNAME = "marketplace"
$env:DB_PASSWORD = "marketplace_dev"
.\mvnw.cmd -s .mvn\settings.xml "-Dmaven.compiler.fork=true" spring-boot:run
```

Para detenerlo:

```powershell
docker compose down
```

## Pruebas

```powershell
.\mvnw.cmd -s .mvn\settings.xml "-Dmaven.compiler.fork=true" test
```
