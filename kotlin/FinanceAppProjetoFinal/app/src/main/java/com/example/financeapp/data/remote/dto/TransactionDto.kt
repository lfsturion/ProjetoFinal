package com.example.financeapp.data.remote.dto

data class TransactionDto(
    val id: Long,
    val description: String,
    val amount: Double,
    val date: String,
    val type: String
)