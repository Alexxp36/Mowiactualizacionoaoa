package com.miempresa.mowimarket.data.models

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null
)
