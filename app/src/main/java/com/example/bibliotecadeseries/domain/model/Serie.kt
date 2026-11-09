package com.example.bibliotecadeseries.domain.model

data class Serie(
    val id: Int,
    val titulo: String,
    val genero: String,
    val plataforma: String,
    val temporadasTotales: Int,
    val temporadasVistas: Int,
    val favorita: Boolean
) {
    val estado: EstadoSerie
        get() = when {
            temporadasVistas <= 0 -> EstadoSerie.POR_VER
            temporadasVistas >= temporadasTotales -> EstadoSerie.TERMINADA
            else -> EstadoSerie.VIENDO
        }

    val avance: Float
        get() = if (temporadasTotales == 0) 0f else temporadasVistas.toFloat() / temporadasTotales
}