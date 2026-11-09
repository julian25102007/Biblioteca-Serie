package com.example.bibliotecadeseries.data.repository

import com.example.bibliotecadeseries.data.mapper.toDomain
import com.example.bibliotecadeseries.data.mapper.toDomainList
import com.example.bibliotecadeseries.data.mapper.toRequestDto
import com.example.bibliotecadeseries.data.remote.SeriesApiService
import com.example.bibliotecadeseries.data.remote.safeApiCall
import com.example.bibliotecadeseries.domain.model.DatosSerie
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.domain.repository.SeriesRepository
import com.example.bibliotecadeseries.domain.result.AppResult

class SeriesRepositoryImpl(
    private val apiService: SeriesApiService
) : SeriesRepository {

    override suspend fun getSeries(): AppResult<List<Serie>> =
        safeApiCall { apiService.getSeries().toDomainList() }

    override suspend fun getSerie(id: Int): AppResult<Serie> =
        safeApiCall { apiService.getSerie(id).toDomain() }

    override suspend fun createSerie(datos: DatosSerie): AppResult<Serie> =
        safeApiCall { apiService.createSerie(datos.toRequestDto()).toDomain() }

    override suspend fun updateSerie(id: Int, datos: DatosSerie): AppResult<Serie> =
        safeApiCall { apiService.updateSerie(id, datos.toRequestDto()).toDomain() }

    override suspend fun deleteSerie(id: Int): AppResult<Unit> =
        safeApiCall { apiService.deleteSerie(id) }
}