package com.miempresa.mowimarket.data.models

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("categoria")
    val categoria: Int,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("vendidos")
    val vendidos: Int = 0,

    @SerializedName("imagen")
    val imagen: String? = null,

    @SerializedName("activo")
    val activo: Boolean = true,

    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null,

    @SerializedName("fecha_actualizacion")
    val fechaActualizacion: String? = null
) {
    val precioFormateado: String
        get() = "S/ %.2f".format(precio)

    val disponible: Boolean
        get() = activo && stock > 0
}

data class ProductoConCategoria(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("categoria")
    val categoria: Categoria,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("vendidos")
    val vendidos: Int = 0,

    @SerializedName("imagen")
    val imagen: String? = null,

    @SerializedName("activo")
    val activo: Boolean = true
)
