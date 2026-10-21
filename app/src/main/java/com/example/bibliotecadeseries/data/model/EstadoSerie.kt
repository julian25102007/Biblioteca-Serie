package com.example.bibliotecadeseries.ui.biblioteca.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.bibliotecadeseries.R
import com.example.bibliotecadeseries.data.model.EstadoSerie

@Composable
fun EstadoSerie.etiqueta(): String = stringResource(
    when (this) {
        EstadoSerie.POR_VER -> R.string.estado_por_ver
        EstadoSerie.VIENDO -> R.string.estado_viendo
        EstadoSerie.TERMINADA -> R.string.estado_terminada
    }
)