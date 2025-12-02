package com.miempresa.mowimarket.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miempresa.mowimarket.data.models.Carrito
import com.miempresa.mowimarket.data.models.CrearPedidoRequest
import com.miempresa.mowimarket.data.models.DetallePedidoRequest
import com.miempresa.mowimarket.data.preferences.TokenManager
import com.miempresa.mowimarket.data.repository.MowiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarritoViewModel(context: Context) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val repository = MowiRepository(tokenManager)

    private val _carrito = MutableStateFlow<Carrito?>(null)
    val carrito: StateFlow<Carrito?> = _carrito.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargarCarrito() {
        val usuario = tokenManager.getUser()
        if (usuario == null) {
            _mensaje.value = "Debes iniciar sesión"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getCarrito(usuario.id)
            result.onSuccess {
                _carrito.value = it
            }.onFailure {
                _mensaje.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun agregarProducto(productoId: Int, cantidad: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.agregarAlCarrito(productoId, cantidad)
            result.onSuccess {
                _mensaje.value = "Producto agregado al carrito"
                cargarCarrito()
            }.onFailure {
                _mensaje.value = it.message ?: "Error al agregar producto"
            }
            _isLoading.value = false
        }
    }

    fun actualizarCantidad(itemId: Int, cantidad: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.actualizarItemCarrito(itemId, cantidad)
            result.onSuccess {
                cargarCarrito()
            }.onFailure {
                _mensaje.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun eliminarItem(itemId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.eliminarItemCarrito(itemId)
            result.onSuccess {
                _mensaje.value = "Producto eliminado"
                cargarCarrito()
            }.onFailure {
                _mensaje.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun crearPedido(metodoPago: String) {
        val usuario = tokenManager.getUser()
        val carritoActual = _carrito.value

        if (usuario == null || carritoActual == null || carritoActual.items.isEmpty()) {
            _mensaje.value = "El carrito está vacío"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            val detalles = carritoActual.items.map { item ->
                DetallePedidoRequest(
                    producto = item.producto,
                    cantidad = item.cantidad,
                    precioUnitario = item.productoPrecio ?: 0.0
                )
            }

            val request = CrearPedidoRequest(
                usuario = usuario.id,
                total = carritoActual.total,
                metodoPago = metodoPago,
                detalles = detalles
            )

            val result = repository.crearPedido(request)
            result.onSuccess {
                _mensaje.value = "¡Pedido creado exitosamente!"
                _carrito.value = null
            }.onFailure {
                _mensaje.value = it.message ?: "Error al crear pedido"
            }

            _isLoading.value = false
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
