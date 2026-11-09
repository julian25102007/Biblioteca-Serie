package com.example.bibliotecadeseries.ui.components

import androidx.compose.ui.graphics.Color
import com.example.bibliotecadeseries.domain.model.EstadoSerie
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

data class PaletaEstado(val inicio: Color, val fin: Color, val acento: Color)

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

fun EstadoSerie.paleta(): PaletaEstado = when (this) {
    EstadoSerie.POR_VER -> PaletaEstado(Color(0xFFD6DEFF), Color(0xFFB9C6FF), Color(0xFF4457D6))
    EstadoSerie.VIENDO -> PaletaEstado(Color(0xFFFFDDB8), Color(0xFFFFB98A), Color(0xFFD9480F))
    EstadoSerie.TERMINADA -> PaletaEstado(Color(0xFFC9F0D3), Color(0xFF9EDDB0), Color(0xFF2B9348))
}