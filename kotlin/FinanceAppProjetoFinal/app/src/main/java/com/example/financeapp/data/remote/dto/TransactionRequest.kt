package com.example.financeapp.data.remote.dto

import java.math.BigDecimal

data class TransactionRequest(
    val id: Long? = null,
    val description: String,
    val amount: BigDecimal,
    val date: String,
    val type: String
)