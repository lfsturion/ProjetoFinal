package com.example.financeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.repository.AuthRepository
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.model.TransactionType
import com.example.financeapp.domain.repository.TransactionRepository
import com.example.financeapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime

class HomeViewModel(
    private val repository: TransactionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init {
        viewModelScope.launch {
            repository.loadTransactions()
        }

        viewModelScope.launch {
            repository.getTransactions().collect { result ->

                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            loading = true,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        _state.value = HomeUiState(
                            transactions = result.data ?: emptyList(),
                            loading = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        _state.value = HomeUiState(
                            transactions = emptyList(),
                            loading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun addTransaction(
        description: String,
        amount: BigDecimal,
        date: LocalDateTime,
        isIncome: Boolean
    ) {
        viewModelScope.launch {
            repository.insertTransaction(
                Transaction(
                    id = 0,
                    description = description,
                    amount = amount,
                    date = date,
                    type = if (isIncome)
                        TransactionType.INCOME
                    else
                        TransactionType.EXPENSE
                )
            )
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction.id)
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLogout()
        }
    }
}
