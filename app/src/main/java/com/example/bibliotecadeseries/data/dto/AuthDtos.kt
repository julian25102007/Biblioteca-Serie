package com.example.bibliotecadeseries.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val nombre: String,
    val correo: String,
    val contrasena: String
)

@Serializable
data class LoginRequestDto(
    val correo: String,
    val contrasena: String
)

@Serializable
data class UserDto(
    val id: Int,
    val nombre: String,
    val correo: String
)

@Serializable
data class AuthResponseDto(
    val token: String,
    val user: UserDto
)