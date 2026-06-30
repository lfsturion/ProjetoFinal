package com.example.financeapp.domain.usecase

import com.example.financeapp.domain.repository.TransactionRepository

class GetTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getTransactions()
}