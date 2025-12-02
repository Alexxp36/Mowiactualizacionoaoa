package com.miempresa.mowimarket.data.repository

import com.miempresa.mowimarket.data.api.RetrofitClient
import com.miempresa.mowimarket.data.models.*
import com.miempresa.mowimarket.data.preferences.TokenManager

/**
 * Repositorio centralizado para todas las operaciones de la app
 */
class MowiRepository(private val tokenManager: TokenManager) {

    private val api = RetrofitClient.mowiApiService

    private fun getAuthHeader(): String {
        return "Bearer ${tokenManager.getAccessToken()}"
    }

    // ==================== CATEGORÍAS ====================
    suspend fun getCategorias(): Result<List<Categoria>> {
        return try {
            val response = api.getCategorias()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener categorías"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== PRODUCTOS ====================
    suspend fun getProductos(
        search: String? = null,
        categoria: Int? = null,
        precioMin: Double? = null,
        precioMax: Double? = null
    ): Result<List<Producto>> {
        return try {
            val response = api.getProductos(search, categoria, precioMin, precioMax)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProducto(id: Int): Result<Producto> {
        return try {
            val response = api.getProducto(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearProducto(producto: Producto): Result<Producto> {
        return try {
            val response = api.crearProducto(getAuthHeader(), producto)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== CARRITO ====================
    suspend fun getCarrito(usuarioId: Int): Result<Carrito> {
        return try {
            val response = api.getCarrito(getAuthHeader(), usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener carrito"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun agregarAlCarrito(productoId: Int, cantidad: Int = 1): Result<ItemCarrito> {
        return try {
            val request = AgregarItemCarritoRequest(productoId, cantidad)
            val response = api.agregarItemCarrito(getAuthHeader(), request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al agregar al carrito"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarItemCarrito(itemId: Int, cantidad: Int): Result<ItemCarrito> {
        return try {
            val request = ActualizarItemCarritoRequest(cantidad)
            val response = api.actualizarItemCarrito(getAuthHeader(), itemId, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarItemCarrito(itemId: Int): Result<Unit> {
        return try {
            val response = api.eliminarItemCarrito(getAuthHeader(), itemId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== PEDIDOS ====================
    suspend fun getPedidos(usuarioId: Int? = null): Result<List<Pedido>> {
        return try {
            val response = api.getPedidos(getAuthHeader(), usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener pedidos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearPedido(request: CrearPedidoRequest): Result<Pedido> {
        return try {
            val response = api.crearPedido(getAuthHeader(), request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear pedido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== DASHBOARD ADMIN ====================
    suspend fun getVentasPorCategoria(): Result<List<VentasPorCategoria>> {
        return try {
            val response = api.getVentasPorCategoria(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener ventas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductosMasVendidos(): Result<List<ProductoMasVendido>> {
        return try {
            val response = api.getProductosMasVendidos(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener productos más vendidos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsuariosActivosSemana(): Result<List<UsuariosActivosSemana>> {
        return try {
            val response = api.getUsuariosActivosSemana(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener usuarios activos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsuarios(): Result<List<UserData>> {
        return try {
            val response = api.getUsuarios(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener usuarios"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarProducto(id: Int): Result<Unit> {
        return try {
            val response = api.eliminarProducto(getAuthHeader(), id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarEstadoPedido(pedidoId: Int, estado: String): Result<Pedido> {
        return try {
            val request = mapOf("estado" to estado)
            val response = api.actualizarPedido(getAuthHeader(), pedidoId, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar pedido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
