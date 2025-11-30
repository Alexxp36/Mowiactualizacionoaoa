package com.miempresa.mowimarket.ui.screens.support

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.FAQ
import com.miempresa.mowimarket.data.models.FAQCategory
import com.miempresa.mowimarket.ui.components.MowiTopBarWithBack
import com.miempresa.mowimarket.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(onNavigateBack: () -> Unit) {
    var expandedFaqId by remember { mutableStateOf<String?>(null) }

    // Mock FAQs
    val faqs = remember {
        listOf(
            FAQ("1", "¿Cómo puedo rastrear mi pedido?", "Puedes rastrear tu pedido ingresando a tu cuenta y visitando la sección 'Mis Pedidos'.", FAQCategory.ORDERS),
            FAQ("2", "¿Cuánto tiempo tarda la entrega?", "El tiempo de entrega varía según tu ubicación. Generalmente entre 2 a 5 días hábiles.", FAQCategory.ORDERS),
            FAQ("3", "¿Qué métodos de pago aceptan?", "Aceptamos tarjetas de crédito, débito, transferencias bancarias y pagos contra entrega.", FAQCategory.PAYMENTS),
            FAQ("4", "¿Puedo devolver un producto?", "Sí, tienes 30 días desde la recepción del producto para solicitar una devolución.", FAQCategory.RETURNS)
        )
    }

    Scaffold(
        topBar = {
            MowiTopBarWithBack(
                title = "Centro de Soporte",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Centro de Soporte",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "¿En qué podemos ayudarte hoy?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            }

            // FAQ Title
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MowiOrange
                        )
                        Text(
                            text = "Preguntas Frecuentes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // FAQ List
            items(faqs) { faq ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            expandedFaqId = if (expandedFaqId == faq.id) null else faq.id
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (expandedFaqId == faq.id) MowiOrangeLight.copy(alpha = 0.1f) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(MowiOrange, CircleShape)
                                )
                                Text(
                                    text = faq.category.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MowiOrange,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Icon(
                                imageVector = if (expandedFaqId == faq.id) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = faq.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (expandedFaqId == faq.id) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = faq.answer,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Contact Section
            item {
                Spacer(modifier = Modifier.height(32.dp))

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contacta con Nosotros",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ContactCard(
                            icon = Icons.Default.Call,
                            title = "Chat en Vivo",
                            subtitle = "Disponible 24/7",
                            modifier = Modifier.weight(1f)
                        )

                        ContactCard(
                            icon = Icons.Default.Phone,
                            title = "+51 999 123 456",
                            subtitle = "Lun-Vie 9AM-6PM",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ContactCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BackgroundSecondary),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MowiOrange,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}
