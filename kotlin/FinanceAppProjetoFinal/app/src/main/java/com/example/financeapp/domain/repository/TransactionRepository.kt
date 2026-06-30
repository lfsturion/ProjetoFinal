package com.example.financeapp.domain.repository

import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getTransactions(): Flow<Resource<List<Transaction>>>

    suspend fun loadTransactions()

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(id: Long)

    suspend fun updateTransaction(transaction: Transaction)
}