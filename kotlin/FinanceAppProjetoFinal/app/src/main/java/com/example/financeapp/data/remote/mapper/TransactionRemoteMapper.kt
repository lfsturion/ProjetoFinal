package com.example.financeapp.data.remote.mapper

import com.example.financeapp.data.remote.dto.TransactionDto
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.model.TransactionType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

fun Transaction.toDto(): TransactionDto {
    return TransactionDto(
        id = id,
        description = description,
        amount = amount.toDouble(),
        date = date.toString(),
        type = type.name
    )
}

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = id,
        description = description,
        amount = amount.toBigDecimal(),
        date = LocalDateTime.parse(date, formatter),
        type = TransactionType.valueOf(type)
    )
}