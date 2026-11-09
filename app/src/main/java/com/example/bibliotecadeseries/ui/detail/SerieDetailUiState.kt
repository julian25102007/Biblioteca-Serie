package com.example.bibliotecadeseries.ui.detail

import com.example.bibliotecadeseries.domain.model.Serie

data class SerieDetailUiState(
    val isLoading: Boolean = false,
    val serie: Serie? = null,
    val errorMessage: String? = null
)