package com.example.financeapp.data.repository

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun logout()
}