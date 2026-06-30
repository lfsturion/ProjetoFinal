package com.example.financeapp.data.repository

import com.example.financeapp.data.remote.api.AuthApi
import com.example.financeapp.data.local.datastore.TokenDataStore
import com.example.financeapp.data.remote.dto.LoginRequest

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    override suspend fun login(email: String, password: String) {

        val response = api.login(
            LoginRequest(email, password)
        )

        val token = response.data.token
        val refreshToken = response.data.refreshToken

        tokenDataStore.saveTokens(
            accessToken = token,
            refreshToken = refreshToken
        )
    }

    override suspend fun logout() {
        tokenDataStore.clear()
    }
}