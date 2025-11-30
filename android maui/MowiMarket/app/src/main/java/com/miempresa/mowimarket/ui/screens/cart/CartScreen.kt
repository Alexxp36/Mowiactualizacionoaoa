package com.miempresa.mowimarket.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.ui.components.MowiButton
import com.miempresa.mowimarket.ui.components.MowiTopBarWithBack
import com.miempresa.mowimarket.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onContinueShopping: () -> Unit
) {
    // Mock cart vacío
    val isCartEmpty = true

    Scaffold(
        topBar = {
            MowiTopBarWithBack(
                title = "Carrito de Compras",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        if (isCartEmpty) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(60.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Carrito vacío",
                        modifier = Modifier.size(60.dp),
                        tint = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Agrega algunos productos para continuar con tu compra",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                MowiButton(
                    text = "Continuar comprando",
                    onClick = onContinueShopping,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }
}
