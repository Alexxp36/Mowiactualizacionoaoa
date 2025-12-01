package com.miempresa.mowimarket.data.repository

import com.google.gson.Gson
import com.miempresa.mowimarket.data.api.RetrofitClient
import com.miempresa.mowimarket.data.models.*
import com.miempresa.mowimarket.data.preferences.TokenManager

class AuthRepository(private val tokenManager: TokenManager) {

    private val authApiService = RetrofitClient.authApiService
    private val gson = Gson()

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val loginRequest = LoginRequest(email = email.trim(), password = password)
            val response = authApiService.login(loginRequest)

            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    // Guardar tokens
                    tokenManager.saveTokens(
                        accessToken = authResponse.accessToken,
                        refreshToken = authResponse.refreshToken
                    )

                    // Convertir UserData a User y guardar
                    val user = authResponse.user.toUser()
                    tokenManager.saveUser(user)

                    Result.success(user)
                } else {
                    Result.failure(Exception("Error: Respuesta vacía del servidor"))
                }
            } else {
                // Parsear error
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error ?: "Error desconocido"
                    } catch (e: Exception) {
                        "Error al iniciar sesión"
                    }
                } else {
                    "Error al iniciar sesión"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<String> {
        return try {
            val registerRequest = RegisterRequest(
                name = name.trim(),
                email = email.trim(),
                password = password
            )
            val response = authApiService.register(registerRequest)

            if (response.isSuccessful) {
                val registerResponse = response.body()
                Result.success(registerResponse?.message ?: "Usuario creado exitosamente")
            } else {
                // Parsear error
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                        when {
                            errorResponse.emailErrors != null -> errorResponse.emailErrors.firstOrNull() ?: "Email inválido"
                            errorResponse.passwordErrors != null -> errorResponse.passwordErrors.firstOrNull() ?: "Contraseña inválida"
                            errorResponse.nameErrors != null -> errorResponse.nameErrors.firstOrNull() ?: "Nombre inválido"
                            errorResponse.error != null -> errorResponse.error
                            else -> "Error al registrar usuario"
                        }
                    } catch (e: Exception) {
                        "Error al registrar usuario"
                    }
                } else {
                    "Error al registrar usuario"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    fun logout() {
        tokenManager.clearAll()
    }

    fun getCurrentUser(): User? {
        return tokenManager.getUser()
    }

    fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}
