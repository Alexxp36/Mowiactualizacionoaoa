package com.miempresa.mowimarket.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.Product
import com.miempresa.mowimarket.ui.components.*
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.theme.TextSecondary
import com.miempresa.mowimarket.ui.theme.TextWhite
import com.miempresa.mowimarket.ui.viewmodels.MainViewModel
import com.miempresa.mowimarket.ui.viewmodels.ProductosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProducts: (String?) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val mainViewModel = remember { MainViewModel(context) }
    val productosViewModel = remember { ProductosViewModel(context) }
    val scope = rememberCoroutineScope()

    val currentUser by mainViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null

    // Estados del drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Estados de productos y categorías
    val productos by productosViewModel.productos.collectAsState()
    val categorias by productosViewModel.categorias.collectAsState()
    val isLoading by productosViewModel.isLoading.collectAsState()

    var categoriaSeleccionada by remember { mutableStateOf<Int?>(null) }
    val scrollState = rememberScrollState()

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        productosViewModel.cargarCategorias()
        productosViewModel.cargarProductos()
    }

    // Filtrar productos según categoría seleccionada
    val productosFiltrados = remember(productos, categoriaSeleccionada) {
        if (categoriaSeleccionada == null) {
            productos.take(6) // Mostrar solo 6 productos destacados
        } else {
            productos.filter { it.categoria == categoriaSeleccionada }
        }
    }

    // Convertir a modelo Product para compatibilidad con ProductCard
    val productosCompat = productosFiltrados.map { producto ->
        Product(
            id = producto.id.toString(),
            name = producto.nombre,
            description = producto.descripcion,
            price = producto.precio,
            imageUrl = producto.imagen ?: "",
            category = categorias.find { it.id == producto.categoria }?.nombre ?: "",
            rating = 4.5f,
            reviewCount = producto.vendidos
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                // Header del drawer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MowiOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Categorías",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Opción "Todas"
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = {
                        Text(
                            "Todos los productos",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    selected = categoriaSeleccionada == null,
                    onClick = {
                        categoriaSeleccionada = null
                        scope.launch {
                            drawerState.close()
                            productosViewModel.cargarProductos()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MowiOrange.copy(alpha = 0.2f),
                        selectedIconColor = MowiOrange,
                        selectedTextColor = MowiOrange
                    )
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Lista de categorías
                if (categorias.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = MowiOrange,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Text(
                                "No hay categorías disponibles",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        items(
                            items = categorias,
                            key = { it.id }
                        ) { categoria ->
                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Category, contentDescription = null) },
                                label = {
                                    Column {
                                        Text(
                                            categoria.nombre,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                        if (categoria.descripcion.isNotBlank()) {
                                            Text(
                                                categoria.descripcion,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextSecondary,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                },
                                selected = categoriaSeleccionada == categoria.id,
                                onClick = {
                                    categoriaSeleccionada = categoria.id
                                    scope.launch {
                                        drawerState.close()
                                        productosViewModel.filtrarPorCategoria(categoria.id)
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 12.dp),
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MowiOrange.copy(alpha = 0.2f),
                                    selectedIconColor = MowiOrange,
                                    selectedTextColor = MowiOrange
                                )
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "MOWI Market",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú de categorías",
                                tint = MowiOrange
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToCart) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                        if (isLoggedIn) {
                            IconButton(onClick = onNavigateToProfile) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Perfil"
                                )
                            }
                        } else {
                            TextButton(onClick = onNavigateToAuth) {
                                Text("Ingresar", color = MowiOrange)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                // Hero Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MowiOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = if (categoriaSeleccionada == null) {
                                "¡Grandes ofertas en MOWI!"
                            } else {
                                categorias.find { it.id == categoriaSeleccionada }?.nombre ?: "Productos"
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (categoriaSeleccionada == null) {
                                "Descubre productos increíbles a precios únicos"
                            } else {
                                categorias.find { it.id == categoriaSeleccionada }?.descripcion ?: ""
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextWhite.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Productos
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (categoriaSeleccionada == null) "Productos Destacados" else "Productos",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        if (categoriaSeleccionada == null) {
                            TextButton(onClick = { onNavigateToProducts(null) }) {
                                Text(
                                    text = "Ver todos",
                                    color = MowiOrange
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MowiOrange)
                        }
                    } else if (productosCompat.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No hay productos disponibles",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextSecondary
                                )
                            }
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(productosCompat) { product ->
                                ProductCard(
                                    product = product,
                                    onProductClick = { /* TODO */ },
                                    onAddToCart = { /* TODO */ }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
