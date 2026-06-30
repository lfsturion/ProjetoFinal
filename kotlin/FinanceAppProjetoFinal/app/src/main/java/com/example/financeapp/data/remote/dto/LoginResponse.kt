package com.example.financeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val data: LoginDataDto
)

data class LoginDataDto(
    val token: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    val user: UserDto
)