package com.miempresa.mowimarket.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.Pedido
import com.miempresa.mowimarket.data.repository.MowiRepository
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.theme.TextSecondary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPedidosScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { MowiRepository(context) }

    var pedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var filtroEstado by remember { mutableStateOf<String?>(null) }

    // Cargar pedidos al iniciar
    LaunchedEffect(Unit) {
        isLoading = true
        val result = repository.getPedidos()
        result.onSuccess {
            pedidos = it
            isLoading = false
        }.onFailure {
            errorMessage = it.message
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Pedidos",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            isLoading = true
                            val result = repository.getPedidos()
                            result.onSuccess {
                                pedidos = it
                                isLoading = false
                            }.onFailure {
                                errorMessage = it.message
                                isLoading = false
                            }
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
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
        ) {
            // Filtros por estado
            ScrollableTabRow(
                selectedTabIndex = when (filtroEstado) {
                    null -> 0
                    "pendiente" -> 1
                    "procesando" -> 2
                    "enviado" -> 3
                    "entregado" -> 4
                    else -> 0
                },
                containerColor = Color.White,
                contentColor = MowiOrange,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = filtroEstado == null,
                    onClick = { filtroEstado = null },
                    text = { Text("Todos") }
                )
                Tab(
                    selected = filtroEstado == "pendiente",
                    onClick = { filtroEstado = "pendiente" },
                    text = { Text("Pendiente") }
                )
                Tab(
                    selected = filtroEstado == "procesando",
                    onClick = { filtroEstado = "procesando" },
                    text = { Text("Procesando") }
                )
                Tab(
                    selected = filtroEstado == "enviado",
                    onClick = { filtroEstado = "enviado" },
                    text = { Text("Enviado") }
                )
                Tab(
                    selected = filtroEstado == "entregado",
                    onClick = { filtroEstado = "entregado" },
                    text = { Text("Entregado") }
                )
            }

            Divider()

            // Mensaje de error
            if (errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMessage ?: "",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Lista de pedidos
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MowiOrange)
                }
            } else {
                val pedidosFiltrados = if (filtroEstado == null) {
                    pedidos
                } else {
                    pedidos.filter { it.estado.equals(filtroEstado, ignoreCase = true) }
                }

                if (pedidosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay pedidos",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(pedidosFiltrados) { pedido ->
                            PedidoAdminCard(pedido = pedido)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PedidoAdminCard(pedido: Pedido) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pedido #${pedido.id}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pedido.fechaPedido ?: "Sin fecha",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                EstadoBadge(estado = pedido.estado)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "S/ ${String.format("%.2f", pedido.total)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MowiOrange
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Dirección de Envío",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = pedido.direccionEnvio ?: "No especificada",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Ver detalles */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MowiOrange
                    )
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver Detalles")
                }

                Button(
                    onClick = { /* TODO: Cambiar estado */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MowiOrange
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Actualizar")
                }
            }
        }
    }
}

@Composable
private fun EstadoBadge(estado: String) {
    val (color, text) = when (estado.lowercase()) {
        "pendiente" -> Color(0xFFED8936) to "Pendiente"
        "procesando" -> Color(0xFF4299E1) to "Procesando"
        "enviado" -> Color(0xFF9F7AEA) to "Enviado"
        "entregado" -> Color(0xFF38A169) to "Entregado"
        "cancelado" -> Color(0xFFC53030) to "Cancelado"
        else -> Color.Gray to estado
    }

    AssistChip(
        onClick = { },
        label = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.2f),
            labelColor = color
        )
    )
}
