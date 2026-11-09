package com.example.bibliotecadeseries.ui.components

import androidx.compose.ui.graphics.Color
import com.example.bibliotecadeseries.domain.model.EstadoSerie
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

fun EstadoSerie.etiqueta(): String = when (this) {
    EstadoSerie.POR_VER -> "Por ver"
    EstadoSerie.VIENDO -> "Viendo"
    EstadoSerie.TERMINADA -> "Terminada"
}

fun EstadoSerie.color(): Color = when (this) {
    EstadoSerie.POR_VER -> BibliotecaColores.AzulSuave
    EstadoSerie.VIENDO -> BibliotecaColores.NaranjaSuave
    EstadoSerie.TERMINADA -> BibliotecaColores.VerdeSuave
}