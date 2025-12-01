package com.miempresa.mowimarket.data.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: String? = null,

    @SerializedName("email")
    val emailErrors: List<String>? = null,

    @SerializedName("password")
    val passwordErrors: List<String>? = null,

    @SerializedName("name")
    val nameErrors: List<String>? = null
)
