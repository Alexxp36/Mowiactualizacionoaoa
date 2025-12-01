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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.ui.components.MowiButton
import com.miempresa.mowimarket.ui.components.MowiOutlinedButton
import com.miempresa.mowimarket.ui.components.MowiTextField
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.viewmodels.AuthUiState
import com.miempresa.mowimarket.ui.viewmodels.AuthViewModel

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()

    var isLoginTab by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    // Observar cambios en el estado
    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                onLoginSuccess()
                viewModel.resetUiState()
            }
            is AuthUiState.RegisterSuccess -> {
                // Cambiar a la pestaña de login después de registro exitoso
                isLoginTab = true
            }
            else -> {}
        }
    }

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
                        // Mostrar mensaje de error
                        if (uiState is AuthUiState.Error) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = (uiState as AuthUiState.Error).message,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        // Mostrar mensaje de éxito en registro
                        if (uiState is AuthUiState.RegisterSuccess) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                                )
                            ) {
                                Text(
                                    text = (uiState as AuthUiState.RegisterSuccess).message + "\nAhora puedes iniciar sesión",
                                    color = Color(0xFF2E7D32),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        if (isLoginTab) {
                            LoginForm(
                                viewModel = viewModel,
                                isLoading = uiState is AuthUiState.Loading
                            )
                        } else {
                            RegisterForm(
                                viewModel = viewModel,
                                isLoading = uiState is AuthUiState.Loading
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginForm(
    viewModel: AuthViewModel,
    isLoading: Boolean
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MowiTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            placeholder = "tu@email.com",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            enabled = !isLoading
        )

        MowiTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "Tu contraseña",
            isPassword = true,
            imeAction = ImeAction.Done,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        MowiButton(
            text = if (isLoading) "Iniciando sesión..." else "Iniciar Sesión",
            onClick = {
                viewModel.login(email, password)
            },
            enabled = !isLoading
        )

        if (!isLoading) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.bodyMedium,
                color = MowiOrange,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RegisterForm(
    viewModel: AuthViewModel,
    isLoading: Boolean
) {
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
            imeAction = ImeAction.Next,
            enabled = !isLoading
        )

        MowiTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            placeholder = "tu@email.com",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            enabled = !isLoading
        )

        MowiTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "Mínimo 6 caracteres",
            isPassword = true,
            imeAction = ImeAction.Next,
            enabled = !isLoading
        )

        MowiTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar Contraseña",
            placeholder = "Confirma tu contraseña",
            isPassword = true,
            imeAction = ImeAction.Done,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        MowiButton(
            text = if (isLoading) "Creando cuenta..." else "Crear Cuenta Nueva",
            onClick = {
                viewModel.register(fullName, email, password, confirmPassword)
            },
            enabled = !isLoading
        )
    }
}
