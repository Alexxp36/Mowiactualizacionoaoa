# 📚 Guía de Implementación - MowiMarket Android

Esta guía explica cómo usar todas las funcionalidades implementadas en la aplicación.

## 🏗️ Arquitectura Implementada

La aplicación sigue el patrón **MVVM (Model-View-ViewModel)** con **Repository Pattern**:

```
UI Layer (Compose)
    ↓
ViewModels (State Management)
    ↓
Repository (Business Logic)
    ↓
API Service (Retrofit)
    ↓
Backend API (Django)
```

## 📦 Componentes Implementados

### 1. Modelos de Datos

Ubicación: `app/src/main/java/com/miempresa/mowimarket/data/models/`

- **Categoria.kt** - Categorías de productos
- **Producto.kt** - Productos con precio, stock, etc.
- **Pedido.kt** - Pedidos con detalles
- **Carrito.kt** - Carrito de compras con items
- **Dashboard.kt** - Modelos para analytics (admin)
- **User.kt** - Modelo de usuario actualizado
- **AuthResponse.kt** - Respuestas de autenticación

### 2. API Services

Ubicación: `app/src/main/java/com/miempresa/mowimarket/data/api/`

#### **MowiApiService.kt** - API completa con endpoints:

**Categorías:**
- `getCategorias()` - Listar todas las categorías
- `getCategoria(id)` - Obtener una categoría

**Productos:**
- `getProductos(search, categoria, precioMin, precioMax)` - Listar productos con filtros
- `getProducto(id)` - Detalle de un producto
- `crearProducto()` - Crear producto (admin)
- `actualizarProducto()` - Actualizar producto (admin)
- `eliminarProducto()` - Eliminar producto (admin)

**Carrito:**
- `getCarrito(usuarioId)` - Obtener carrito del usuario
- `agregarItemCarrito()` - Agregar producto al carrito
- `actualizarItemCarrito()` - Cambiar cantidad
- `eliminarItemCarrito()` - Quitar item del carrito

**Pedidos:**
- `getPedidos(usuarioId, estado)` - Listar pedidos con filtros
- `getPedido(id)` - Detalle de un pedido
- `crearPedido()` - Crear nuevo pedido
- `actualizarPedido()` - Cambiar estado (admin)

**Admin:**
- `getUsuarios()` - Listar usuarios
- `getVentasPorCategoria()` - Analytics de ventas
- `getProductosMasVendidos()` - Top productos

### 3. Repository

Ubicación: `app/src/main/java/com/miempresa/mowimarket/data/repository/MowiRepository.kt`

**Repositorio centralizado** que maneja todas las llamadas a la API con manejo de errores y tokens de autenticación.

```kotlin
val repository = MowiRepository(tokenManager)

// Ejemplos de uso:
val productos = repository.getProductos(search = "laptop")
val carrito = repository.getCarrito(usuarioId = 1)
val pedidos = repository.getPedidos(usuarioId = 1)
```

### 4. ViewModels

Ubicación: `app/src/main/java/com/miempresa/mowimarket/ui/viewmodels/`

#### **MainViewModel.kt**
Gestiona el usuario actual y estado global:
```kotlin
val mainViewModel = MainViewModel(context)

// Verificar si está logueado
if (mainViewModel.isLoggedIn()) { ... }

// Verificar si es admin
if (mainViewModel.isAdmin()) { ... }

// Cerrar sesión
mainViewModel.logout()
```

#### **ProductosViewModel.kt**
Gestiona catálogo de productos:
```kotlin
val viewModel = ProductosViewModel(context)

// Observar productos
val productos by viewModel.productos.collectAsState()

// Buscar productos
viewModel.buscarProductos("laptop")

// Filtrar por categoría
viewModel.filtrarPorCategoria(categoriaId = 1)

// Cargar con filtros
viewModel.cargarProductos(
    search = "laptop",
    categoriaId = 1,
    precioMin = 100.0,
    precioMax = 1000.0
)
```

#### **CarritoViewModel.kt**
Gestiona el carrito de compras:
```kotlin
val viewModel = CarritoViewModel(context)

// Observar carrito
val carrito by viewModel.carrito.collectAsState()

// Cargar carrito
viewModel.cargarCarrito()

// Agregar producto
viewModel.agregarProducto(productoId = 5, cantidad = 2)

// Actualizar cantidad
viewModel.actualizarCantidad(itemId = 10, cantidad = 3)

// Eliminar item
viewModel.eliminarItem(itemId = 10)

// Crear pedido
viewModel.crearPedido(metodoPago = "tarjeta")
```

#### **AuthViewModel.kt**
Gestiona autenticación (ya existente):
```kotlin
val viewModel = AuthViewModel(context)

// Login
viewModel.login(email, password)

// Registro
viewModel.register(name, email, password, confirmPassword)

// Observar estado
val uiState by viewModel.uiState.collectAsState()
```

## 💡 Cómo Usar en las Pantallas

### Ejemplo: Pantalla de Productos

```kotlin
@Composable
fun ProductsScreen() {
    val context = LocalContext.current
    val viewModel = remember { ProductosViewModel(context) }

    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column {
        // Filtros de categoría
        CategoriasList(
            categorias = categorias,
            onCategoriaClick = { categoriaId ->
                viewModel.filtrarPorCategoria(categoriaId)
            }
        )

        // Barra de búsqueda
        SearchBar(
            onSearch = { query ->
                viewModel.buscarProductos(query)
            }
        )

        // Lista de productos
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(productos) { producto ->
                    ProductCard(
                        producto = producto,
                        onClick = { /* Navegar a detalles */ }
                    )
                }
            }
        }
    }
}
```

### Ejemplo: Pantalla de Carrito

```kotlin
@Composable
fun CartScreen() {
    val context = LocalContext.current
    val viewModel = remember { CarritoViewModel(context) }

    val carrito by viewModel.carrito.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarCarrito()
    }

    Column {
        // Items del carrito
        carrito?.items?.forEach { item ->
            CartItemCard(
                item = item,
                onIncrement = {
                    viewModel.actualizarCantidad(item.id, item.cantidad + 1)
                },
                onDecrement = {
                    viewModel.actualizarCantidad(item.id, item.cantidad - 1)
                },
                onRemove = {
                    viewModel.eliminarItem(item.id)
                }
            )
        }

        // Total
        Text("Total: ${carrito?.totalFormateado ?: "S/ 0.00"}")

        // Botón de compra
        Button(
            onClick = { viewModel.crearPedido("tarjeta") }
        ) {
            Text("Finalizar Compra")
        }
    }

    // Mostrar mensaje
    mensaje?.let {
        Snackbar(message = it)
        viewModel.limpiarMensaje()
    }
}
```

### Ejemplo: Detección de Rol (Admin vs Cliente)

```kotlin
@Composable
fun MainApp() {
    val context = LocalContext.current
    val mainViewModel = remember { MainViewModel(context) }
    val navController = rememberNavController()

    val currentUser by mainViewModel.currentUser.collectAsState()

    // Determinar pantalla inicial según rol
    val startDestination = when {
        currentUser == null -> Screen.Home.route  // Sin login
        mainViewModel.isAdmin() -> Screen.AdminDashboard.route  // Admin
        else -> Screen.Home.route  // Cliente normal
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(/* ... */)
        }

        composable(Screen.AdminDashboard.route) {
            // Solo accesible si es admin
            if (mainViewModel.isAdmin()) {
                AdminDashboardScreen(/* ... */)
            } else {
                // Redirigir si no es admin
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route)
                }
            }
        }
    }
}
```

## 🎯 Próximos Pasos para Mejorar

1. **Conectar ProductsScreen con ProductosViewModel**
   - Ya están los modelos y el ViewModel
   - Solo falta integrar en la UI

2. **Conectar CartScreen con CarritoViewModel**
   - Cargar items del carrito al abrir
   - Permitir modificar cantidades
   - Implementar checkout

3. **Crear AdminDashboardScreen**
   - Usar MowiRepository para cargar KPIs
   - Mostrar gráficos con los datos
   - Implementar gestión de productos/pedidos

4. **Implementar pantalla de Pedidos**
   - Mostrar historial de pedidos del usuario
   - Ver detalles de cada pedido
   - Filtrar por estado

5. **Mejorar navegación**
   - Agregar bottom navigation bar
   - Tabs para admin (Dashboard, Productos, Pedidos, Usuarios)
   - Drawer navigation opcional

## 📊 Endpoints Disponibles

Todos estos endpoints ya están implementados en `MowiApiService.kt`:

| Endpoint | Método | Descripción |
|----------|---------|-------------|
| `/api/dashboard/categorias/` | GET | Listar categorías |
| `/api/dashboard/productos/` | GET | Listar productos con filtros |
| `/api/dashboard/productos/{id}/` | GET | Detalle de producto |
| `/api/dashboard/carrito/` | GET | Obtener carrito |
| `/api/dashboard/items-carrito/` | POST | Agregar item |
| `/api/dashboard/items-carrito/{id}/` | PUT/DELETE | Actualizar/Eliminar item |
| `/api/dashboard/pedidos/` | GET/POST | Listar/Crear pedidos |
| `/api/dashboard/usuarios/` | GET | Listar usuarios (admin) |
| `/api/dashboard/ventas-por-categoria/` | GET | Analytics (admin) |
| `/api/dashboard/productos-mas-vendidos/` | GET | Top productos (admin) |

## 🔑 Autenticación

Todos los endpoints protegidos requieren el token JWT en el header:
```
Authorization: Bearer {access_token}
```

El `MowiRepository` se encarga automáticamente de agregar este header usando el `TokenManager`.

## ✅ Checklist de Implementación

- [x] Modelos de datos completos
- [x] API Service con Retrofit
- [x] Repository Pattern
- [x] ViewModels con StateFlow
- [x] Autenticación funcional
- [x] Manejo de tokens JWT
- [x] Detección de rol (Admin/Cliente)
- [ ] Pantallas conectadas con ViewModels
- [ ] AdminDashboard con KPIs
- [ ] Gestión de productos (admin)
- [ ] Gestión de pedidos (admin)
- [ ] Perfil de usuario
- [ ] Historial de pedidos

## 🚀 ¡La infraestructura está lista!

Todo el código base está implementado. Solo falta conectar las pantallas existentes con los ViewModels para traer datos reales de la API.

**Ejemplo rápido para empezar:**
```kotlin
// En ProductsScreen.kt, reemplaza los datos mock con:
val viewModel = remember { ProductosViewModel(LocalContext.current) }
val productos by viewModel.productos.collectAsState()

// Listo! Ahora ProductsScreen muestra productos reales de la base de datos
```

---

**Documentación creada:** 2025-12-02
**Versión:** 1.0 - Infraestructura Completa
