package com.miempresa.mowimarket.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miempresa.mowimarket.data.models.User
import com.miempresa.mowimarket.data.preferences.TokenManager
import com.miempresa.mowimarket.data.repository.AuthRepository
import com.miempresa.mowimarket.data.repository.MowiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val authRepository = AuthRepository(tokenManager)
    val mowiRepository = MowiRepository(tokenManager)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Cargar usuario actual si existe
        _currentUser.value = authRepository.getCurrentUser()
    }

    fun isLoggedIn(): Boolean {
        return _currentUser.value != null && authRepository.isLoggedIn()
    }

    fun isAdmin(): Boolean {
        return _currentUser.value?.isAdmin == true || _currentUser.value?.isStaff == true
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }

    fun refreshUser() {
        _currentUser.value = authRepository.getCurrentUser()
    }
}
