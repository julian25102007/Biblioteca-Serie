package com.example.bibliotecadeseries.data.remote

import com.example.bibliotecadeseries.data.dto.SerieDto
import com.example.bibliotecadeseries.data.dto.SerieRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SeriesApiService {

    @GET("series")
    suspend fun getSeries(): List<SerieDto>

    @GET("series/{id}")
    suspend fun getSerie(@Path("id") id: Int): SerieDto

    @POST("series")
    suspend fun createSerie(@Body body: SerieRequestDto): SerieDto

    @PUT("series/{id}")
    suspend fun updateSerie(@Path("id") id: Int, @Body body: SerieRequestDto): SerieDto

    @DELETE("series/{id}")
    suspend fun deleteSerie(@Path("id") id: Int)
}