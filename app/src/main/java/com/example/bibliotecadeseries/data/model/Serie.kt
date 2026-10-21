package com.example.bibliotecadeseries.data.model

data class Serie(
    val id: Long,
    val titulo: String,
    val genero: String,
    val temporadasTotales: Int,
    val temporadasVistas: Int = 0
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