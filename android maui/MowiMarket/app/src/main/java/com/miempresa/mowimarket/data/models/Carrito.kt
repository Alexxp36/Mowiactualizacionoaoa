package com.miempresa.mowimarket.data.models

import com.google.gson.annotations.SerializedName

data class Carrito(
    @SerializedName("id")
    val id: Int,

    @SerializedName("usuario")
    val usuario: Int,

    @SerializedName("items")
    val items: List<ItemCarrito> = emptyList(),

    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null
) {
    val total: Double
        get() = items.sumOf { it.subtotal }

    val totalFormateado: String
        get() = "S/ %.2f".format(total)

    val cantidadTotal: Int
        get() = items.sumOf { it.cantidad }
}

data class ItemCarrito(
    @SerializedName("id")
    val id: Int,

    @SerializedName("carrito")
    val carrito: Int,

    @SerializedName("producto")
    val producto: Int,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("producto_nombre")
    val productoNombre: String? = null,

    @SerializedName("producto_precio")
    val productoPrecio: Double? = null,

    @SerializedName("producto_imagen")
    val productoImagen: String? = null,

    @SerializedName("producto_stock")
    val productoStock: Int? = null
) {
    val subtotal: Double
        get() = (productoPrecio ?: 0.0) * cantidad

    val subtotalFormateado: String
        get() = "S/ %.2f".format(subtotal)
}

data class AgregarItemCarritoRequest(
    @SerializedName("producto")
    val producto: Int,

    @SerializedName("cantidad")
    val cantidad: Int = 1
)

data class ActualizarItemCarritoRequest(
    @SerializedName("cantidad")
    val cantidad: Int
)
