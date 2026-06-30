package com.example.financeapp.data.remote.interceptor

import com.example.financeapp.data.local.datastore.TokenDataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: TokenDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getAccessTokenSync()
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
        return chain.proceed(request)
    }
}