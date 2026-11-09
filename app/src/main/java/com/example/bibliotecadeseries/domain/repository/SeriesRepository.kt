package com.example.bibliotecadeseries.domain.repository

import com.example.bibliotecadeseries.domain.model.DatosSerie
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.domain.result.AppResult

interface SeriesRepository {
    suspend fun getSeries(): AppResult<List<Serie>>
    suspend fun getSerie(id: Int): AppResult<Serie>
    suspend fun createSerie(datos: DatosSerie): AppResult<Serie>
    suspend fun updateSerie(id: Int, datos: DatosSerie): AppResult<Serie>
    suspend fun deleteSerie(id: Int): AppResult<Unit>
}