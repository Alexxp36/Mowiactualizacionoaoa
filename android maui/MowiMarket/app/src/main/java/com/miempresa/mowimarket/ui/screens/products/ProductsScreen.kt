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
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.Product
import com.miempresa.mowimarket.ui.components.MowiTopBarWithBack
import com.miempresa.mowimarket.ui.components.ProductCard
import com.miempresa.mowimarket.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onNavigateBack: () -> Unit,
    onProductClick: (String) -> Unit
) {
    // Mock data
    val products = remember {
        listOf(
            Product(
                id = "1",
                name = "iPhone 15 Pro Max",
                description = "",
                price = 4299.99,
                imageUrl = "",
                category = "Tecnología",
                rating = 4.8f,
                reviewCount = 234
            ),
            Product(
                id = "2",
                name = "Laptop ASUS ROG",
                description = "",
                price = 2899.99,
                imageUrl = "",
                category = "Tecnología",
                rating = 4.8f,
                reviewCount = 156
            )
        )
    }

    Scaffold(
        topBar = {
            MowiTopBarWithBack(
                title = "Catálogo de Productos",
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
                    text = "${products.size} productos disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Divider()

            // Products Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
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
