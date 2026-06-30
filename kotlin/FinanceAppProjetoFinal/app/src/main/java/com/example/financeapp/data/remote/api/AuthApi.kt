package com.example.financeapp.data.remote.api

import com.example.financeapp.data.remote.dto.LoginRequest
import com.example.financeapp.data.remote.dto.LoginResponse
import com.example.financeapp.data.remote.dto.RefreshRequest
import com.example.financeapp.data.remote.dto.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("refresh.php")
    suspend fun refresh(
        @Body request: RefreshRequest
    ): RefreshResponse

    @POST("logout.php")
    suspend fun logout(): Response<Unit>
}