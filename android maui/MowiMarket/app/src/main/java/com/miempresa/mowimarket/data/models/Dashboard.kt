package com.miempresa.mowimarket.data.models

import com.google.gson.annotations.SerializedName

data class VentasPorCategoria(
    @SerializedName("categoria__nombre")
    val categoriaNombre: String,

    @SerializedName("total_ventas")
    val totalVentas: Double,

    @SerializedName("cantidad_productos")
    val cantidadProductos: Int
)

data class ProductoMasVendido(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("vendidos")
    val vendidos: Int,

    @SerializedName("porcentaje")
    val porcentaje: Double
)

data class UsuariosActivosSemana(
    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("count")
    val count: Int
)

data class EstadisticasAdmin(
    val totalProductos: Int,
    val totalPedidos: Int,
    val totalVentas: Double,
    val productosbajoStock: Int,
    val ventasPorCategoria: List<VentasPorCategoria>,
    val productosMasVendidos: List<ProductoMasVendido>,
    val usuariosActivosSemana: List<UsuariosActivosSemana>
)
