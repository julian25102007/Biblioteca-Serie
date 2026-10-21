package com.example.bibliotecadeseries.biblioteca

import com.example.bibliotecadeseries.data.model.EstadoSerie
import com.example.bibliotecadeseries.data.model.Serie

enum class Pantalla { LISTA, AGREGAR }

data class FormularioSerie(
    val titulo: String = "",
    val genero: String = "",
    val temporadas: String = ""
)

data class BibliotecaUiState(
    val series: List<Serie> = emptyList(),
    val filtro: EstadoSerie? = null,
    val pantalla: Pantalla = Pantalla.LISTA,
    val formulario: FormularioSerie = FormularioSerie(),
    val errorFormulario: Boolean = false,
    val total: Int = 0,
    val terminadas: Int = 0,
    val temporadasVistas: Int = 0,
    val temporadasTotales: Int = 0,
    val avanceGeneral: Int = 0
)