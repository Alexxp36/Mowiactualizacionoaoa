package com.miempresa.mowimarket.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miempresa.mowimarket.data.models.User
import com.miempresa.mowimarket.data.preferences.TokenManager
import com.miempresa.mowimarket.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val authRepository = AuthRepository(tokenManager)

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        // Cargar usuario actual si existe
        _currentUser.value = authRepository.getCurrentUser()
    }

    fun login(email: String, password: String) {
        // Validaciones básicas
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor completa todos los campos")
            return
        }

        if (!isValidEmail(email)) {
            _uiState.value = AuthUiState.Error("El email no es válido")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(email, password)

            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _uiState.value = AuthUiState.Success("¡Bienvenido ${user.name}!")
                },
                onFailure = { exception ->
                    _uiState.value = AuthUiState.Error(exception.message ?: "Error al iniciar sesión")
                }
            )
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        // Validaciones
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor completa todos los campos")
            return
        }

        if (name.length < 2) {
            _uiState.value = AuthUiState.Error("El nombre debe tener al menos 2 caracteres")
            return
        }

        if (!isValidEmail(email)) {
            _uiState.value = AuthUiState.Error("El email no es válido")
            return
        }

        if (password.length < 6) {
            _uiState.value = AuthUiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.register(name, email, password)

            result.fold(
                onSuccess = { message ->
                    _uiState.value = AuthUiState.RegisterSuccess(message)
                },
                onFailure = { exception ->
                    _uiState.value = AuthUiState.Error(exception.message ?: "Error al registrar usuario")
                }
            )
        }
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
        _uiState.value = AuthUiState.Idle
    }

    fun resetUiState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val message: String) : AuthUiState()
    data class RegisterSuccess(val message: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
