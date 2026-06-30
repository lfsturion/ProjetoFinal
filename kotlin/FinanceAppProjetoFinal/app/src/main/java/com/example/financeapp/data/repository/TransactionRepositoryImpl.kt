package com.example.financeapp.data.repository

import com.example.financeapp.data.remote.api.TransactionApi
import com.example.financeapp.data.remote.dto.DeleteRequest
import com.example.financeapp.data.remote.dto.TransactionRequest
import com.example.financeapp.data.remote.mapper.toDomain
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.TransactionRepository
import com.example.financeapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionRepositoryImpl(
    private val api: TransactionApi
) : TransactionRepository {

    private val _transactions =
        MutableStateFlow<Resource<List<Transaction>>>(Resource.Loading())

    override fun getTransactions(): Flow<Resource<List<Transaction>>> {
        return _transactions.asStateFlow()
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        try {
            api.insertTransaction(
                TransactionRequest(
                    description = transaction.description,
                    amount = transaction.amount,
                    date = transaction.date.toString(),
                    type = transaction.type.name
                )
            )
            loadTransactions()
        } catch (e: Exception) {
            _transactions.value = Resource.Error("Erro ao inserir")
        }
    }

    override suspend fun deleteTransaction(id: Long) {
        try {
            api.deleteTransaction(DeleteRequest(id))
            loadTransactions()
        } catch (e: Exception) {
            _transactions.value = Resource.Error("Erro ao deletar")
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        try {
            api.updateTransaction(
                TransactionRequest(
                    id = transaction.id,
                    description = transaction.description,
                    amount = transaction.amount,
                    date = transaction.date.toString(),
                    type = transaction.type.name
                )
            )
            loadTransactions()
        } catch (e: Exception) {
            _transactions.value = Resource.Error("Erro ao atualizar")
        }
    }

    override suspend fun loadTransactions() {
        try {
            _transactions.value = Resource.Loading()
            val response = api.getTransactions()
            _transactions.value = Resource.Success(
                response.map { it.toDomain() }
            )
        } catch (e: Exception) {
            _transactions.value = Resource.Error("Erro ao carregar dados")
        }
    }

    suspend fun initLoad() {
        loadTransactions()
    }
}