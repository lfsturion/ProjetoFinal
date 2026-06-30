package com.example.financeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    val token: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)