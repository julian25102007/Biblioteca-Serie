package com.example.bibliotecadeseries.ui.form

data class SerieFormUiState(
    val esEdicion: Boolean = false,
    val titulo: String = "",
    val genero: String = "",
    val plataforma: String = "",
    val temporadasTotales: String = "",
    val temporadasVistas: String = "0",
    val favorita: Boolean = false,
    val errorTitulo: String? = null,
    val errorGenero: String? = null,
    val errorTemporadasTotales: String? = null,
    val errorTemporadasVistas: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorCarga: String? = null,
    val userMessage: String? = null,
    val mensajeGuardado: String? = null
)