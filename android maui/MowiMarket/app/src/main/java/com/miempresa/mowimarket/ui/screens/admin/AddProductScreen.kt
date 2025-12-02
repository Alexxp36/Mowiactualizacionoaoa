package com.miempresa.mowimarket.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.miempresa.mowimarket.data.models.Categoria
import com.miempresa.mowimarket.data.models.Producto
import com.miempresa.mowimarket.data.preferences.TokenManager
import com.miempresa.mowimarket.data.repository.MowiRepository
import com.miempresa.mowimarket.ui.theme.MowiOrange
import com.miempresa.mowimarket.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onNavigateBack: () -> Unit,
    onProductSaved: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }
    val repository = remember { MowiRepository(tokenManager) }
    val scrollState = rememberScrollState()

    var categorias by remember { mutableStateOf<List<Categoria>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Form fields
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoriaId by remember { mutableStateOf<Int?>(null) }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    // Validation errors
    var nombreError by remember { mutableStateOf<String?>(null) }
    var categoriaError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }
    var imagenError by remember { mutableStateOf<String?>(null) }

    // Cargar categorías
    LaunchedEffect(Unit) {
        isLoading = true
        val result = repository.getCategorias()
        result.onSuccess {
            categorias = it
            isLoading = false
        }.onFailure {
            errorMessage = "Error al cargar categorías"
            isLoading = false
        }
    }

    fun validateForm(): Boolean {
        nombreError = when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            else -> null
        }

        categoriaError = if (categoriaId == null) "Debe seleccionar una categoría" else null

        precioError = when {
            precio.isBlank() -> "El precio es requerido"
            precio.toDoubleOrNull() == null -> "Precio inválido"
            precio.toDouble() <= 0 -> "El precio debe ser mayor a 0"
            else -> null
        }

        stockError = when {
            stock.isBlank() -> "El stock es requerido"
            stock.toIntOrNull() == null -> "Stock inválido"
            stock.toInt() < 0 -> "El stock no puede ser negativo"
            else -> null
        }

        imagenError = if (imagenUrl.isNotBlank()) {
            try {
                java.net.URL(imagenUrl)
                null
            } catch (e: Exception) {
                "URL de imagen inválida"
            }
        } else null

        return nombreError == null && categoriaError == null &&
                precioError == null && stockError == null && imagenError == null
    }

    fun saveProduct() {
        if (!validateForm()) {
            errorMessage = "Por favor, corrija los errores en el formulario"
            return
        }

        isSaving = true
        errorMessage = null

        scope.launch {
            try {
                val producto = Producto(
                    id = 0, // El backend asignará el ID
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    categoria = categoriaId!!,
                    precio = precio.toDouble(),
                    stock = stock.toInt(),
                    imagen = imagenUrl.trim().ifBlank { null },
                    activo = activo,
                    vendidos = 0
                )

                val result = repository.crearProducto(producto)
                result.onSuccess {
                    showSuccessDialog = true
                }.onFailure {
                    errorMessage = "Error al guardar el producto: ${it.message}"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isSaving = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Nuevo Producto",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Completa los datos del producto",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7FAFC))
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MowiOrange
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            // Error message
                            if (errorMessage != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = errorMessage!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                            // Preview de imagen
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .background(
                                        Color(0xFFE2E8F0),
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Image,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = Color(0xFF718096)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // URL de imagen
                            OutlinedTextField(
                                value = imagenUrl,
                                onValueChange = {
                                    imagenUrl = it
                                    imagenError = null
                                },
                                label = { Text("URL de imagen (opcional)") },
                                placeholder = { Text("https://ejemplo.com/imagen.jpg") },
                                leadingIcon = {
                                    Icon(Icons.Default.Image, contentDescription = null)
                                },
                                isError = imagenError != null,
                                supportingText = imagenError?.let {
                                    { Text(it, color = MaterialTheme.colorScheme.error) }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Nombre
                            OutlinedTextField(
                                value = nombre,
                                onValueChange = {
                                    nombre = it
                                    nombreError = null
                                },
                                label = { Text("Nombre del Producto *") },
                                placeholder = { Text("Ej: iPhone 15 Pro Max") },
                                leadingIcon = {
                                    Icon(Icons.Default.ShoppingBag, contentDescription = null)
                                },
                                isError = nombreError != null,
                                supportingText = nombreError?.let {
                                    { Text(it, color = MaterialTheme.colorScheme.error) }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Descripción
                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text("Descripción") },
                                placeholder = { Text("Describe las características del producto...") },
                                leadingIcon = {
                                    Icon(Icons.Default.Description, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Categoría
                            var expanded by remember { mutableStateOf(false) }
                            val selectedCategoria = categorias.find { it.id == categoriaId }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedCategoria?.nombre ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Categoría *") },
                                    placeholder = { Text("Seleccionar categoría") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Category, contentDescription = null)
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                    },
                                    isError = categoriaError != null,
                                    supportingText = categoriaError?.let {
                                        { Text(it, color = MaterialTheme.colorScheme.error) }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    categorias.forEach { categoria ->
                                        DropdownMenuItem(
                                            text = { Text(categoria.nombre) },
                                            onClick = {
                                                categoriaId = categoria.id
                                                categoriaError = null
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Precio y Stock
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Precio
                                OutlinedTextField(
                                    value = precio,
                                    onValueChange = {
                                        precio = it
                                        precioError = null
                                    },
                                    label = { Text("Precio (S/) *") },
                                    placeholder = { Text("0.00") },
                                    leadingIcon = {
                                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                                    },
                                    isError = precioError != null,
                                    supportingText = precioError?.let {
                                        { Text(it, color = MaterialTheme.colorScheme.error) }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )

                                // Stock
                                OutlinedTextField(
                                    value = stock,
                                    onValueChange = {
                                        stock = it
                                        stockError = null
                                    },
                                    label = { Text("Stock *") },
                                    placeholder = { Text("0") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Inventory, contentDescription = null)
                                    },
                                    isError = stockError != null,
                                    supportingText = stockError?.let {
                                        { Text(it, color = MaterialTheme.colorScheme.error) }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Estado activo
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF7FAFC)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (activo) MowiOrange else Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Estado del Producto",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = if (activo) "Activo" else "Inactivo",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                    Switch(
                                        checked = activo,
                                        onCheckedChange = { activo = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = MowiOrange
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(24.dp))

                            // Botones
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onNavigateBack,
                                    modifier = Modifier.weight(1f),
                                    enabled = !isSaving
                                ) {
                                    Text("Cancelar")
                                }

                                Button(
                                    onClick = { saveProduct() },
                                    modifier = Modifier.weight(1f),
                                    enabled = !isSaving,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MowiOrange
                                    )
                                ) {
                                    if (isSaving) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(if (isSaving) "Guardando..." else "Crear Producto")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF38A169),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "¡Producto Creado!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("El producto ha sido agregado exitosamente al catálogo.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onProductSaved()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MowiOrange
                    )
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
