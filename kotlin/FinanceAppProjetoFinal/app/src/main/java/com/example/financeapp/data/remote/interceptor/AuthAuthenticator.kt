package com.example.financeapp.data.remote.interceptor

import com.example.financeapp.data.remote.api.AuthApi
import com.example.financeapp.data.local.datastore.TokenDataStore
import com.example.financeapp.data.remote.dto.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthAuthenticator(
    private val authApi: AuthApi,
    private val sessionManager: TokenDataStore
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        val refreshToken = sessionManager.getRefreshTokenSync()

        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        return runBlocking {
            try {
                val newToken = authApi.refresh(
                    RefreshRequest(refreshToken)
                )

                sessionManager.saveTokens(
                    accessToken = newToken.token,
                    refreshToken = newToken.refreshToken
                )

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newToken.token}")
                    .build()

            } catch (e: Exception) {
                null
            }
        }
    }
}