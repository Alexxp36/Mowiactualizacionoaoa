package com.miempresa.mowimarket.data.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val isAdmin: Boolean = false,
    val isStaff: Boolean = false,
    val phone: String? = null,
    val address: String? = null
) {
    val role: UserRole
        get() = if (isAdmin || isStaff) UserRole.ADMIN else UserRole.CLIENT
}

enum class UserRole {
    CLIENT,
    ADMIN
}

// Extension function to convert UserData to User
fun UserData.toUser() = User(
    id = id,
    name = name,
    email = email,
    isAdmin = isAdmin,
    isStaff = isStaff
)
