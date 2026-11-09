package com.example.bibliotecadeseries.data.remote

import com.example.bibliotecadeseries.data.dto.AuthResponseDto
import com.example.bibliotecadeseries.data.dto.LoginRequestDto
import com.example.bibliotecadeseries.data.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("register")
    suspend fun register(@Body body: RegisterRequestDto): AuthResponseDto

    @POST("login")
    suspend fun login(@Body body: LoginRequestDto): AuthResponseDto
}