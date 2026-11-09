package com.example.bibliotecadeseries.ui.home

import com.example.bibliotecadeseries.domain.model.Serie

data class SeriesUiState(
    val isLoading: Boolean = false,
    val series: List<Serie> = emptyList(),
    val errorMessage: String? = null,
    val serieAEliminar: Serie? = null,
    val userMessage: String? = null,
    val sesionCerrada: Boolean = false
)