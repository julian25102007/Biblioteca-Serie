package com.example.bibliotecadeseries.domain.model

data class DatosSerie(
    val titulo: String,
    val genero: String,
    val plataforma: String,
    val temporadasTotales: Int,
    val temporadasVistas: Int,
    val favorita: Boolean
)