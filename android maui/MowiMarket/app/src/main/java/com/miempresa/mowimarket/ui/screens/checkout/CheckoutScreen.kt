package com.miempresa.mowimarket.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.miempresa.mowimarket.ui.components.MowiButton
import com.miempresa.mowimarket.ui.components.MowiTopBarWithBack
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.theme.TextSecondary
import com.miempresa.mowimarket.ui.viewmodels.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onOrderComplete: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { CarritoViewModel(context) }

    val carrito by viewModel.carrito.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var metodoPagoSeleccionado by remember { mutableStateOf("tarjeta") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Cargar carrito
    LaunchedEffect(Unit) {
        viewModel.cargarCarrito()
    }

    // Escuchar mensajes de éxito
    LaunchedEffect(mensaje) {
        if (mensaje?.contains("exitosamente") == true) {
            showSuccessDialog = true
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        Dialog(onDismissRequest = { }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Éxito",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¡Pedido realizado!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tu pedido ha sido procesado exitosamente. Puedes ver el estado en la sección de pedidos.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    MowiButton(
                        text = "Volver al inicio",
                        onClick = onOrderComplete,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MowiTopBarWithBack(
                title = "Checkout",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        if (isLoading && carrito == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MowiOrange)
            }
        } else if (carrito?.items?.isEmpty() == true) {
            // Si el carrito está vacío, redirigir
            LaunchedEffect(Unit) {
                onNavigateBack()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Resumen de productos
                    item {
                        Text(
                            text = "Resumen de productos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(carrito?.items ?: emptyList()) { item ->
                        ProductoResumenCard(item)
                    }

                    // Método de pago
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Método de pago",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        MetodoPagoOption(
                            metodo = "tarjeta",
                            titulo = "Tarjeta de crédito/débito",
                            icon = Icons.Default.CreditCard,
                            seleccionado = metodoPagoSeleccionado == "tarjeta",
                            onClick = { metodoPagoSeleccionado = "tarjeta" }
                        )
                    }

                    item {
                        MetodoPagoOption(
                            metodo = "yape",
                            titulo = "Yape",
                            icon = Icons.Default.Smartphone,
                            seleccionado = metodoPagoSeleccionado == "yape",
                            onClick = { metodoPagoSeleccionado = "yape" }
                        )
                    }

                    item {
                        MetodoPagoOption(
                            metodo = "transferencia",
                            titulo = "Transferencia bancaria",
                            icon = Icons.Default.AccountBalance,
                            seleccionado = metodoPagoSeleccionado == "transferencia",
                            onClick = { metodoPagoSeleccionado = "transferencia" }
                        )
                    }

                    // Total
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Subtotal",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = carrito?.totalFormateado ?: "S/ 0.00",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = carrito?.totalFormateado ?: "S/ 0.00",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MowiOrange
                                    )
                                }
                            }
                        }
                    }
                }

                // Botón de confirmar pedido
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (isLoading) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MowiOrange)
                            }
                        } else {
                            MowiButton(
                                text = "Confirmar pedido",
                                onClick = {
                                    viewModel.crearPedido(metodoPagoSeleccionado)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoResumenCard(item: com.miempresa.mowimarket.data.models.ItemCarrito) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.productoImagen,
                contentDescription = item.productoNombre,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productoNombre ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cantidad: ${item.cantidad}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Text(
                text = item.subtotalFormateado,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MowiOrange
            )
        }
    }
}

@Composable
fun MetodoPagoOption(
    metodo: String,
    titulo: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (seleccionado) {
                    Modifier.border(
                        width = 2.dp,
                        color = MowiOrange,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) MowiOrange.copy(alpha = 0.1f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = titulo,
                tint = if (seleccionado) MowiOrange else TextSecondary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            RadioButton(
                selected = seleccionado,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MowiOrange
                )
            )
        }
    }
}
