package com.miempresa.mowimarket.data.models

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val role: UserRole = UserRole.CLIENT,
    val phone: String? = null,
    val address: String? = null
)

enum class UserRole {
    CLIENT,
    ADMIN
}
