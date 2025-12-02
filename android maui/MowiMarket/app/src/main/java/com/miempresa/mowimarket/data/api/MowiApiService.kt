package com.miempresa.mowimarket.data.api

import com.miempresa.mowimarket.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface MowiApiService {

    // ==================== CATEGORÍAS ====================
    @GET("api/dashboard/categorias/")
    suspend fun getCategorias(): Response<List<Categoria>>

    @GET("api/dashboard/categorias/{id}/")
    suspend fun getCategoria(@Path("id") id: Int): Response<Categoria>

    // ==================== PRODUCTOS ====================
    @GET("api/dashboard/productos/")
    suspend fun getProductos(
        @Query("search") search: String? = null,
        @Query("categoria") categoria: Int? = null,
        @Query("precio_min") precioMin: Double? = null,
        @Query("precio_max") precioMax: Double? = null
    ): Response<List<Producto>>

    @GET("api/dashboard/productos/{id}/")
    suspend fun getProducto(@Path("id") id: Int): Response<Producto>

    @POST("api/dashboard/productos/")
    suspend fun crearProducto(
        @Header("Authorization") token: String,
        @Body producto: Producto
    ): Response<Producto>

    @PUT("api/dashboard/productos/{id}/")
    suspend fun actualizarProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body producto: Producto
    ): Response<Producto>

    @DELETE("api/dashboard/productos/{id}/")
    suspend fun eliminarProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // ==================== CARRITO ====================
    @GET("api/dashboard/carrito/")
    suspend fun getCarrito(
        @Header("Authorization") token: String,
        @Query("usuario") usuarioId: Int
    ): Response<Carrito>

    @POST("api/dashboard/carrito/")
    suspend fun crearCarrito(
        @Header("Authorization") token: String,
        @Body request: Map<String, Int>
    ): Response<Carrito>

    // Items del carrito
    @POST("api/dashboard/items-carrito/")
    suspend fun agregarItemCarrito(
        @Header("Authorization") token: String,
        @Body request: AgregarItemCarritoRequest
    ): Response<ItemCarrito>

    @PUT("api/dashboard/items-carrito/{id}/")
    suspend fun actualizarItemCarrito(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: ActualizarItemCarritoRequest
    ): Response<ItemCarrito>

    @DELETE("api/dashboard/items-carrito/{id}/")
    suspend fun eliminarItemCarrito(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // ==================== PEDIDOS ====================
    @GET("api/dashboard/pedidos/")
    suspend fun getPedidos(
        @Header("Authorization") token: String,
        @Query("usuario") usuarioId: Int? = null,
        @Query("estado") estado: String? = null
    ): Response<List<Pedido>>

    @GET("api/dashboard/pedidos/{id}/")
    suspend fun getPedido(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Pedido>

    @POST("api/dashboard/pedidos/")
    suspend fun crearPedido(
        @Header("Authorization") token: String,
        @Body request: CrearPedidoRequest
    ): Response<Pedido>

    @PUT("api/dashboard/pedidos/{id}/")
    suspend fun actualizarPedido(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, String>
    ): Response<Pedido>

    // ==================== USUARIOS (ADMIN) ====================
    @GET("api/dashboard/usuarios/")
    suspend fun getUsuarios(
        @Header("Authorization") token: String
    ): Response<List<UserData>>

    @GET("api/dashboard/usuarios/{id}/")
    suspend fun getUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<UserData>

    @PUT("api/dashboard/usuarios/{id}/")
    suspend fun actualizarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, Any>
    ): Response<UserData>

    @DELETE("api/dashboard/usuarios/{id}/")
    suspend fun eliminarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // ==================== DASHBOARD ANALYTICS ====================
    @GET("api/dashboard/ventas-por-categoria/")
    suspend fun getVentasPorCategoria(
        @Header("Authorization") token: String
    ): Response<List<VentasPorCategoria>>

    @GET("api/dashboard/productos-mas-vendidos/")
    suspend fun getProductosMasVendidos(
        @Header("Authorization") token: String
    ): Response<List<ProductoMasVendido>>

    @GET("api/dashboard/usuarios-activos-semana/")
    suspend fun getUsuariosActivosSemana(
        @Header("Authorization") token: String
    ): Response<List<UsuariosActivosSemana>>
}
