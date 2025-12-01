package com.miempresa.mowimarket.data.api

import com.miempresa.mowimarket.data.models.AuthResponse
import com.miempresa.mowimarket.data.models.LoginRequest
import com.miempresa.mowimarket.data.models.RegisterRequest
import com.miempresa.mowimarket.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("api/register/")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}
