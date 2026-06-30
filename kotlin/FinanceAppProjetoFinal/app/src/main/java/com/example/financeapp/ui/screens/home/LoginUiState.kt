package com.example.financeapp.ui.screens.home

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val logged: Boolean = false
)