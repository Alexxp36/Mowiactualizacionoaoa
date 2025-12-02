package com.miempresa.mowimarket.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.Categories
import com.miempresa.mowimarket.data.models.Product
import com.miempresa.mowimarket.ui.components.*
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.theme.TextWhite
import com.miempresa.mowimarket.ui.viewmodels.MainViewModel

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

    val currentUser by mainViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null

    val scrollState = rememberScrollState()

    // Mock data de productos destacados
    val featuredProducts = remember {
        listOf(
            Product(
                id = "1",
                name = "iPhone 15 Pro Max",
                description = "El último iPhone con chip A17 Pro",
                price = 4299.99,
                originalPrice = 4899.99,
                imageUrl = "",
                category = "Tecnología",
                brand = "Apple",
                rating = 4.8f,
                reviewCount = 234,
                stock = 10,
                isFeatured = true
            ),
            Product(
                id = "2",
                name = "Laptop ASUS ROG",
                description = "Gaming laptop de alto rendimiento",
                price = 2899.99,
                imageUrl = "",
                category = "Tecnología",
                brand = "ASUS",
                rating = 4.6f,
                reviewCount = 156,
                stock = 5,
                isFeatured = true
            )
        )
    }

    Scaffold(
        topBar = {
            MowiTopBar(
                onCartClick = onNavigateToCart,
                onLoginClick = onNavigateToAuth,
                onProfileClick = onNavigateToProfile,
                cartItemCount = 0,
                isLoggedIn = isLoggedIn,
                userName = currentUser?.name
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
                    .height(300.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "¡Grandes ofertas en MOWI!",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Descubre productos increíbles a precios únicos",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextWhite.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { onNavigateToProducts(null) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MowiOrange
                        )
                    ) {
                        Text(
                            text = "Explorar Productos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Categorías
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(Categories.ALL) { category ->
                        CategoryCard(
                            category = category,
                            onClick = { onNavigateToProducts(it.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Productos Destacados
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Productos Destacados",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = { onNavigateToProducts(null) }) {
                        Text(
                            text = "Ver todos",
                            color = MowiOrange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(featuredProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { /* TODO */ },
                            onAddToCart = { /* TODO */ }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
