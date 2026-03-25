package com.example.eccomerceapp.data.api

import com.example.eccomerceapp.data.model.LoginRequest
import com.example.eccomerceapp.data.model.LoginResponse
import com.example.eccomerceapp.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>
}
