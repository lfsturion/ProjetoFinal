package com.example.financeapp.domain.usecase

import com.example.financeapp.data.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        repository.login(email, password)
    }
}