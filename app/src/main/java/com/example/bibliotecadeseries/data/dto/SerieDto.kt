package com.example.bibliotecadeseries.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SerieDto(
    val id: Int,
    val titulo: String,
    val genero: String,
    val plataforma: String = "",
    @SerialName("temporadas_totales") val temporadasTotales: Int,
    @SerialName("temporadas_vistas") val temporadasVistas: Int = 0,
    val favorita: Boolean = false
)


@Serializable
data class SerieRequestDto(
    val titulo: String,
    val genero: String,
    val plataforma: String,
    @SerialName("temporadas_totales") val temporadasTotales: Int,
    @SerialName("temporadas_vistas") val temporadasVistas: Int,
    val favorita: Boolean
)