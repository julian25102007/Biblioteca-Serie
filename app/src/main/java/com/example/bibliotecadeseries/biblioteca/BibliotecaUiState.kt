package com.example.bibliotecadeseries.ui.biblioteca

import com.example.bibliotecadeseries.data.model.Serie

data class BibliotecaUiState(
    val series: List<Serie> = emptyList(),
    val total: Int = 0,
    val terminadas: Int = 0,
    val temporadasVistas: Int = 0,
    val temporadasTotales: Int = 0,
    val avanceGeneral: Int = 0
)