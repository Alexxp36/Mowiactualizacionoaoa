package com.miempresa.mowimarket.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.ui.components.MowiTopBarWithBack
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.theme.TextSecondary
import com.miempresa.mowimarket.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { MainViewModel(context) }
    val currentUser by viewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MowiTopBarWithBack(
                title = "Mi Perfil",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // User Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MowiOrange.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MowiOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Name
                    Text(
                        text = currentUser?.name ?: "Usuario",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // User Email
                    Text(
                        text = currentUser?.email ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )

                    // Admin Badge
                    if (viewModel.isAdmin()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = if (currentUser?.isAdmin == true) "Administrador" else "Staff",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MowiOrange.copy(alpha = 0.2f),
                                labelColor = MowiOrange,
                                leadingIconContentColor = MowiOrange
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // User Information Section
            ProfileSection(title = "Información Personal") {
                ProfileInfoItem(
                    icon = Icons.Default.Person,
                    label = "Nombre de usuario",
                    value = currentUser?.username ?: "N/A"
                )
                Divider()
                ProfileInfoItem(
                    icon = Icons.Default.Email,
                    label = "Correo electrónico",
                    value = currentUser?.email ?: "N/A"
                )
                Divider()
                ProfileInfoItem(
                    icon = Icons.Default.Badge,
                    label = "Tipo de cuenta",
                    value = when {
                        currentUser?.isAdmin == true -> "Administrador"
                        currentUser?.isStaff == true -> "Staff"
                        else -> "Cliente"
                    }
                )
            }

            // Account Settings Section
            ProfileSection(title = "Configuración") {
                ProfileActionItem(
                    icon = Icons.Default.ShoppingBag,
                    label = "Mis Pedidos",
                    onClick = { /* TODO: Navigate to orders */ }
                )
                Divider()
                ProfileActionItem(
                    icon = Icons.Default.Favorite,
                    label = "Lista de Deseos",
                    onClick = { /* TODO: Navigate to wishlist */ }
                )
                Divider()
                ProfileActionItem(
                    icon = Icons.Default.LocationOn,
                    label = "Direcciones",
                    onClick = { /* TODO: Navigate to addresses */ }
                )
                Divider()
                ProfileActionItem(
                    icon = Icons.Default.CreditCard,
                    label = "Métodos de Pago",
                    onClick = { /* TODO: Navigate to payment methods */ }
                )
            }

            // App Settings Section
            ProfileSection(title = "Aplicación") {
                ProfileActionItem(
                    icon = Icons.Default.Notifications,
                    label = "Notificaciones",
                    onClick = { /* TODO: Navigate to notifications settings */ }
                )
                Divider()
                ProfileActionItem(
                    icon = Icons.Default.Lock,
                    label = "Privacidad y Seguridad",
                    onClick = { /* TODO: Navigate to privacy settings */ }
                )
                Divider()
                ProfileActionItem(
                    icon = Icons.Default.Help,
                    label = "Ayuda y Soporte",
                    onClick = { /* TODO: Navigate to support */ }
                )
            }

            // Logout Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                TextButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar Sesión",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(text = "Cerrar Sesión")
            },
            text = {
                Text(text = "¿Estás seguro que deseas cerrar sesión?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MowiOrange,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfileActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MowiOrange,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}
