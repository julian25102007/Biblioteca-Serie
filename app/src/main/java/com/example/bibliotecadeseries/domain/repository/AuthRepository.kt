package com.example.bibliotecadeseries.domain.repository

import com.example.bibliotecadeseries.domain.model.User
import com.example.bibliotecadeseries.domain.result.AppResult

interface AuthRepository {
    suspend fun register(nombre: String, correo: String, contrasena: String): AppResult<User>
    suspend fun login(correo: String, contrasena: String): AppResult<User>
    fun isLoggedIn(): Boolean
    fun logout()
}