package com.example.bibliotecadeseries.data.mapper

import com.example.bibliotecadeseries.data.dto.SerieDto
import com.example.bibliotecadeseries.data.dto.SerieRequestDto
import com.example.bibliotecadeseries.domain.model.DatosSerie
import com.example.bibliotecadeseries.domain.model.Serie

fun SerieDto.toDomain(): Serie = Serie(
    id = id,
    titulo = titulo,
    genero = genero,
    plataforma = plataforma,
    temporadasTotales = temporadasTotales,
    temporadasVistas = temporadasVistas,
    favorita = favorita
)

fun List<SerieDto>.toDomainList(): List<Serie> = map { it.toDomain() }

fun DatosSerie.toRequestDto(): SerieRequestDto = SerieRequestDto(
    titulo = titulo,
    genero = genero,
    plataforma = plataforma,
    temporadasTotales = temporadasTotales,
    temporadasVistas = temporadasVistas,
    favorita = favorita
)