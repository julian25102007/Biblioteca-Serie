package com.example.bibliotecadeseries.biblioteca.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecadeseries.R
import com.example.bibliotecadeseries.data.model.EstadoSerie
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

@Composable
fun FiltrosEstado(
    filtroActual: EstadoSerie?,
    onFiltroChange: (EstadoSerie?) -> Unit,
    modifier: Modifier = Modifier
) {
    val opciones = listOf(
        null to R.string.filtro_todas,
        EstadoSerie.POR_VER to R.string.estado_por_ver,
        EstadoSerie.VIENDO to R.string.estado_viendo,
        EstadoSerie.TERMINADA to R.string.filtro_terminada
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(BibliotecaColores.CremaOscuro)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        opciones.forEach { (estado, textoId) ->
            PillFiltro(
                texto = stringResource(textoId),
                seleccionado = filtroActual == estado,
                onClick = { onFiltroChange(estado) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PillFiltro(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(50),
        color = if (seleccionado) BibliotecaColores.Tinta else Color.Transparent,
        contentColor = if (seleccionado) BibliotecaColores.Crema else BibliotecaColores.Tinta
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = texto,
                fontSize = 13.sp,
                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}