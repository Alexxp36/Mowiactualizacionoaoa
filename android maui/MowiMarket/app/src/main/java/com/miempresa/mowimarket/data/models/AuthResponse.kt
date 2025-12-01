package com.miempresa.mowimarket.data.models

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("access")
    val accessToken: String,

    @SerializedName("refresh")
    val refreshToken: String,

    @SerializedName("user")
    val user: UserData
)

data class UserData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("is_admin")
    val isAdmin: Boolean,

    @SerializedName("is_staff")
    val isStaff: Boolean
)

data class RegisterResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("user")
    val user: UserData
)
