package com.example.bibliotecadeseries.data.repository

import com.example.bibliotecadeseries.data.dto.AuthResponseDto
import com.example.bibliotecadeseries.data.dto.LoginRequestDto
import com.example.bibliotecadeseries.data.dto.RegisterRequestDto
import com.example.bibliotecadeseries.data.mapper.toDomain
import com.example.bibliotecadeseries.data.remote.AuthApiService
import com.example.bibliotecadeseries.data.remote.safeApiCall
import com.example.bibliotecadeseries.data.session.SessionStorage
import com.example.bibliotecadeseries.domain.model.User
import com.example.bibliotecadeseries.domain.repository.AuthRepository
import com.example.bibliotecadeseries.domain.result.AppResult

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun register(nombre: String, correo: String, contrasena: String): AppResult<User> =
        safeApiCall {
            guardarSesion(apiService.register(RegisterRequestDto(nombre, correo, contrasena)))
        }

    override suspend fun login(correo: String, contrasena: String): AppResult<User> =
        safeApiCall {
            guardarSesion(apiService.login(LoginRequestDto(correo, contrasena)))
        }

    override fun isLoggedIn(): Boolean = sessionStorage.getToken() != null

    override fun logout() = sessionStorage.clear()


    private fun guardarSesion(respuesta: AuthResponseDto): User {
        sessionStorage.saveToken(respuesta.token)
        return respuesta.user.toDomain()
    }
}