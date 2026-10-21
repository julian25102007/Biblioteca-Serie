package com.example.bibliotecadeseries.data.repository

import com.example.bibliotecadeseries.data.datasource.BibliotecaDataSource
import com.example.bibliotecadeseries.data.model.Serie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class BibliotecaRepository(private val dataSource: BibliotecaDataSource) {

    val series: Flow<List<Serie>> = dataSource.series

    suspend fun agregar(serie: Serie) {
        dataSource.guardar(series.first() + serie)
    }

    suspend fun cambiarVistas(id: Long, delta: Int) {
        val actualizadas = series.first().map { serie ->
            if (serie.id == id) {
                serie.copy(
                    temporadasVistas = (serie.temporadasVistas + delta)
                        .coerceIn(0, serie.temporadasTotales)
                )
            } else {
                serie
            }
        }
        dataSource.guardar(actualizadas)
    }

    suspend fun eliminar(id: Long) {
        dataSource.guardar(series.first().filterNot { it.id == id })
    }
}