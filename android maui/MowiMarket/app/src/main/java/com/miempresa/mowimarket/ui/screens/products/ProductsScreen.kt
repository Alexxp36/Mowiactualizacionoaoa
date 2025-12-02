package com.miempresa.mowimarket.ui.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.Product
import com.miempresa.mowimarket.ui.components.MowiTopBarWithBack
import com.miempresa.mowimarket.ui.components.ProductCard
import com.miempresa.mowimarket.ui.theme.TextSecondary
import com.miempresa.mowimarket.ui.viewmodels.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onNavigateBack: () -> Unit,
    onProductClick: (String) -> Unit,
    categoryId: String? = null,
    categoryName: String? = null
) {
    val context = LocalContext.current
    val viewModel = remember { ProductosViewModel(context) }

    val productos by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Cargar productos filtrados por categoría si se proporciona
    LaunchedEffect(categoryId, categorias) {
        if (categoryId != null && categorias.isNotEmpty()) {
            // Mapeo de IDs de frontend a nombres de categorías del backend
            val categoryNameMap = mapOf(
                "tech" to "Tecnología",
                "fashion" to "Moda",
                "home" to "Hogar",
                "pets" to "Mascotas",
                "babies" to "Bebés",
                "toys" to "Juguetes"
            )

            // Obtener el nombre de categoría desde el ID de frontend
            val backendCategoryName = categoryNameMap[categoryId] ?: categoryName

            // Buscar el ID numérico de la categoría en el backend por su nombre
            val categoria = categorias.find {
                it.nombre.equals(backendCategoryName, ignoreCase = true) ||
                it.nombre.equals(categoryName, ignoreCase = true)
            }

            if (categoria != null) {
                viewModel.filtrarPorCategoria(categoria.id)
            } else {
                // Si no se encuentra la categoría, cargar todos los productos
                viewModel.cargarProductos()
            }
        } else {
            viewModel.cargarProductos()
        }
    }

    // Convertir productos del nuevo modelo al antiguo para compatibilidad
    val productosCompat = productos.map { producto ->
        Product(
            id = producto.id.toString(),
            name = producto.nombre,
            description = producto.descripcion,
            price = producto.precio,
            imageUrl = producto.imagen ?: "",
            category = "", // Se podría obtener de la categoría
            rating = 4.5f, // Mock por ahora
            reviewCount = 0
        )
    }

    Scaffold(
        topBar = {
            MowiTopBarWithBack(
                title = categoryName ?: "Catálogo de Productos",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = { /* TODO: Filtros */ }) {
                        Icon(Icons.Default.FilterList, "Filtros")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isLoading) "Cargando productos..." else "${productosCompat.size} productos disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Divider()

            // Loading / Products Grid
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (productosCompat.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos disponibles en esta categoría",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 180.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productosCompat) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = onProductClick,
                            onAddToCart = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}
