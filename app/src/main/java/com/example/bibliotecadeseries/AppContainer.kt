package com.example.bibliotecadeseries

import android.content.Context
import com.example.bibliotecadeseries.data.remote.AuthApiService
import com.example.bibliotecadeseries.data.remote.AuthInterceptor
import com.example.bibliotecadeseries.data.remote.SeriesApiService
import com.example.bibliotecadeseries.data.repository.AuthRepositoryImpl
import com.example.bibliotecadeseries.data.repository.SeriesRepositoryImpl
import com.example.bibliotecadeseries.data.session.SessionStorage
import com.example.bibliotecadeseries.data.session.SharedPrefsSessionStorage
import com.example.bibliotecadeseries.domain.repository.AuthRepository
import com.example.bibliotecadeseries.domain.repository.SeriesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit


class AppContainer(context: Context) {

    private val sessionStorage: SessionStorage = SharedPrefsSessionStorage(context)

    // Avisa a la navegación cuando el servidor rechaza el token (sesión expirada).
    private val _sessionExpired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpired: SharedFlow<Unit> = _sessionExpired.asSharedFlow()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionStorage) { _sessionExpired.tryEmit(Unit) })
        // Render (plan gratis) tarda cerca de 1 minuto en despertar tras estar inactivo.
        .connectTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(retrofit.create(AuthApiService::class.java), sessionStorage)
    }

    val seriesRepository: SeriesRepository by lazy {
        SeriesRepositoryImpl(retrofit.create(SeriesApiService::class.java))
    }

    private companion object {

        const val BASE_URL = "https://TU-SERVICIO.onrender.com/"
    }
}