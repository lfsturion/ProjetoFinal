package com.example.financeapp.domain.usecase

import com.example.financeapp.data.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}