# Marketplace Camisetas API

## Credenciales de Administrador (Pruebas)
- **Usuario:** `admin@mail.com`
- **Contraseña:** `Password123!`

---

## Endpoints

### Autenticación
- `POST /api/auth/register` : Registra a un nuevo usuario cliente.
- `POST /api/auth/login` : Inicia sesión y devuelve el Token de seguridad.
- `POST /api/auth/bootstrap-admin` : Inicializa al usuario administrador fundador.

### Usuarios
- `GET /api/usuarios/me` : Muestra datos de tu propia cuenta.
- `PUT /api/usuarios/me` : Modifica tus datos.
- `PATCH /api/usuarios/me/password` : Cambia tu contraseña.
- `DELETE /api/usuarios/me` : Elimina tu cuenta.
- `GET /api/usuarios` : (Solo Admin) Lista el padrón de todos los usuarios registrados.
- `GET o PUT o DELETE /api/usuarios/{id}` : (Solo Admin) Gestiona un usuario de la base de datos.

### Catálogo
*(Nota: Todos pueden hacer GET. Solo Admin puede hacer POST/PUT/DELETE)*
- `/api/catalogo/generos` : Gestiona géneros.
- `/api/catalogo/talles` : Gestiona talles.
- `/api/catalogo/tipos-camiseta` : Gestiona modelos (ej: titular, alternativa).
- `/api/catalogo/paises` : Gestiona selecciones/países.

### Camisetas
- `GET /api/camisetas` : Lista camisetas publicadas.
- `GET /api/camisetas/{id}` : Muestra detalles de una camiseta específica.
- `POST o PUT o DELETE /api/camisetas` : (Solo Admin) Gestiona el catálogo principal de camisetas.
- `GET /api/camisetas/{id}/variantes` : Lista los talles y stocks físicos de una camiseta.
- `POST o PUT o DELETE /api/camisetas/variantes/{id}` : (Solo Admin) Asigna talles y depósito físico a las prendas.
- `PATCH /api/camisetas/variantes/{id}/stock` : (Solo Admin) Modifica únicamente la cifra de stock.
- `POST o PUT o DELETE /api/camisetas/{id}/descuento` : (Solo Admin) Gestiona ofertas promocionales.

### Carrito
- `GET /api/carrito` : Muestra tu carrito actual.
- `POST /api/carrito/items` : Agrega un nuevo producto para compra.
- `PATCH /api/carrito/items/{id}` : Modifica la cantidad de unidades de un ítem en tu carrito.
- `DELETE /api/carrito/items/{id}` : Quita un ítem de tu compra.
- `DELETE /api/carrito` : Vacía al 100% todo tu carrito de compras.

### Pedidos
- `POST /api/pedidos` : Transforma tu carrito en un pedido facturado.
- `GET /api/pedidos` : Visualiza tu historial total de órdenes.
- `GET /api/pedidos/{id}` : Muestra el detalle exacto y comprobante de un pedido finalizado.
- `PATCH /api/pedidos/{id}/cancelar` : Cancela en sistema tu compra iniciada.
- `PATCH /api/pedidos/{id}/estado` : (Solo Admin) Modifica en sistema la fase de envío.
