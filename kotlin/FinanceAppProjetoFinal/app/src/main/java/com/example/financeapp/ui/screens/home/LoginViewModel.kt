package com.example.financeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.financeapp.data.repository.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        state = state.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun login() {
        viewModelScope.launch {
            state = state.copy(
                loading = true,
                error = null
            )

            try {
                authRepository.login(
                    state.email,
                    state.password
                )
                state = state.copy(
                    loading = false,
                    logged = true
                )

            } catch (e: Exception) {
                state = state.copy(
                    loading = false,
                    error = "Falha no login"
                )
            }
        }
    }
}