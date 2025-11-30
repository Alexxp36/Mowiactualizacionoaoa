package com.miempresa.mowimarket.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.ui.components.MowiButton
import com.miempresa.mowimarket.ui.components.MowiOutlinedButton
import com.miempresa.mowimarket.ui.components.MowiTextField
import com.miempresa.mowimarket.ui.theme.MowiOrange

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    var isLoginTab by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.85f)
                    .clickable(enabled = false) { },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, "Cerrar")
                        }
                    }

                    // Title
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Bienvenido a ",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "MOWI",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MowiOrange,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tabs
                    TabRow(
                        selectedTabIndex = if (isLoginTab) 0 else 1,
                        containerColor = Color.Transparent,
                        contentColor = MowiOrange
                    ) {
                        Tab(
                            selected = isLoginTab,
                            onClick = { isLoginTab = true },
                            text = {
                                Text(
                                    "Iniciar Sesión",
                                    fontWeight = if (isLoginTab) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                        Tab(
                            selected = !isLoginTab,
                            onClick = { isLoginTab = false },
                            text = {
                                Text(
                                    "Crear Cuenta",
                                    fontWeight = if (!isLoginTab) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {
                        if (isLoginTab) {
                            LoginForm(onLoginSuccess)
                        } else {
                            RegisterForm(onLoginSuccess)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginForm(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MowiTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            placeholder = "tu@email.com",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        MowiTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "Tu contraseña",
            isPassword = true,
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.height(8.dp))

        MowiButton(text = "Iniciar Sesión", onClick = onLoginSuccess)

        Text(
            text = "¿Olvidaste tu contraseña?",
            style = MaterialTheme.typography.bodyMedium,
            color = MowiOrange,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Divider()

        Text(
            text = "Cuentas de demostración:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        MowiOutlinedButton(text = "Entrar como Cliente", onClick = onLoginSuccess)
        MowiOutlinedButton(text = "Entrar como Administrador", onClick = onLoginSuccess)
    }
}

@Composable
private fun RegisterForm(onRegisterSuccess: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MowiTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Nombre Completo",
            placeholder = "Tu nombre completo",
            imeAction = ImeAction.Next
        )

        MowiTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            placeholder = "tu@email.com",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        MowiTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "Mínimo 6 caracteres",
            isPassword = true,
            imeAction = ImeAction.Next
        )

        MowiTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar Contraseña",
            placeholder = "Confirma tu contraseña",
            isPassword = true,
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.height(8.dp))

        MowiButton(text = "Crear Cuenta Nueva", onClick = onRegisterSuccess)
    }
}
