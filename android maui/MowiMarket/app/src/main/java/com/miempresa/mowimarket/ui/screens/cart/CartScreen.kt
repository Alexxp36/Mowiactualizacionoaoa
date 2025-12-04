package com.miempresa.mowimarket.ui.screens.cart

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.miempresa.mowimarket.data.models.ItemCarrito
import com.miempresa.mowimarket.ui.components.MowiButton
import com.miempresa.mowimarket.ui.components.MowiTopBarWithBack
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.theme.TextSecondary
import com.miempresa.mowimarket.ui.viewmodels.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onContinueShopping: () -> Unit,
    onNavigateToCheckout: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel = remember { CarritoViewModel(context) }

    val carrito by viewModel.carrito.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    // Cargar carrito al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarCarrito()
    }

    // Mostrar mensajes
    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            // Aquí podrías mostrar un Snackbar si lo deseas
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = {
            MowiTopBarWithBack(
                title = "Carrito de Compras",
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
        } else if (carrito == null || carrito?.items?.isEmpty() == true) {
            // Carrito vacío
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
        } else {
            // Carrito con productos
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = carrito?.items ?: emptyList(),
                        key = { it.id }
                    ) { item ->
                        CartItemCard(
                            item = item,
                            onUpdateQuantity = { cantidad ->
                                viewModel.actualizarCantidad(item.id, cantidad)
                            },
                            onRemove = {
                                viewModel.eliminarItem(item.id)
                            }
                        )
                    }
                }

                // Resumen del pedido
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
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
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total de productos",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = carrito?.totalFormateado ?: "S/ 0.00",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MowiOrange
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        MowiButton(
                            text = "Proceder al pago",
                            onClick = onNavigateToCheckout,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: ItemCarrito,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            AsyncImage(
                model = item.productoImagen,
                contentDescription = item.productoNombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.productoNombre ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "S/ %.2f".format(item.productoPrecio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MowiOrange,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controles de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (item.cantidad > 1) {
                                onUpdateQuantity(item.cantidad - 1)
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Disminuir",
                            tint = MowiOrange
                        )
                    }

                    Text(
                        text = item.cantidad.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            val stock = item.productoStock ?: Int.MAX_VALUE
                            if (item.cantidad < stock) {
                                onUpdateQuantity(item.cantidad + 1)
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar",
                            tint = MowiOrange
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Subtotal: ${item.subtotalFormateado}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
