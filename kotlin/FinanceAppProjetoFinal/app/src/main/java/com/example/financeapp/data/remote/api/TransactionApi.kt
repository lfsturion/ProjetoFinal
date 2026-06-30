package com.example.financeapp.data.remote.api

import com.example.financeapp.data.remote.dto.DeleteRequest
import com.example.financeapp.data.remote.dto.TransactionDto
import com.example.financeapp.data.remote.dto.TransactionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TransactionApi {
    @GET("transactions.php")
    suspend fun getTransactions(): List<TransactionDto>

    @POST("insert_transaction.php")
    suspend fun insertTransaction(
        @Body request: TransactionRequest
    )

    @POST("update_transaction.php")
    suspend fun updateTransaction(
        @Body request: TransactionRequest
    )

    @POST("delete_transaction.php")
    suspend fun deleteTransaction(
        @Body request: DeleteRequest
    )
}