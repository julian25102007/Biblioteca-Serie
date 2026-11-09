package com.example.bibliotecadeseries.data.remote

import com.example.bibliotecadeseries.data.session.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(
    private val sessionStorage: SessionStorage,
    private val onSessionExpired: () -> Unit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val esRutaDeAuth = original.url.encodedPath in RUTAS_DE_AUTH
        val token = sessionStorage.getToken()

        val request = if (token == null || esRutaDeAuth) {
            original
        } else {
            original.newBuilder().header("Authorization", "Bearer $token").build()
        }

        val response = chain.proceed(request)

        if (response.code == 401 && token != null && !esRutaDeAuth) {
            sessionStorage.clear()
            onSessionExpired()
        }
        return response
    }

    private companion object {
        val RUTAS_DE_AUTH = setOf("/login", "/register")
    }
}