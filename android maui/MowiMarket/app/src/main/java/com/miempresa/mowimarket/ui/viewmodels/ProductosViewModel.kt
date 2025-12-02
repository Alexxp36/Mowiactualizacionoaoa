package com.miempresa.mowimarket.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miempresa.mowimarket.data.models.Categoria
import com.miempresa.mowimarket.data.models.Producto
import com.miempresa.mowimarket.data.preferences.TokenManager
import com.miempresa.mowimarket.data.repository.MowiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductosViewModel(context: Context) : ViewModel() {

    private val repository = MowiRepository(TokenManager(context))

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarCategorias()
        cargarProductos()
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            val result = repository.getCategorias()
            result.onSuccess { _categorias.value = it }
        }
    }

    fun cargarProductos(
        search: String? = null,
        categoriaId: Int? = null,
        precioMin: Double? = null,
        precioMax: Double? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getProductos(search, categoriaId, precioMin, precioMax)
            result.onSuccess {
                _productos.value = it
            }.onFailure {
                _error.value = it.message ?: "Error al cargar productos"
            }

            _isLoading.value = false
        }
    }

    fun buscarProductos(query: String) {
        cargarProductos(search = query)
    }

    fun filtrarPorCategoria(categoriaId: Int?) {
        cargarProductos(categoriaId = categoriaId)
    }
}
