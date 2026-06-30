package com.example.financeapp.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDateTime,
    val type: TransactionType
)

enum class TransactionType {
    INCOME,
    EXPENSE
}