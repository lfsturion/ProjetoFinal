package com.example.financeapp.ui.screens.home

import com.example.financeapp.domain.model.Transaction

data class HomeUiState(
    val transactions: List<Transaction> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)