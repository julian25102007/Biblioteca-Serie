package com.example.bibliotecadeseries.ui.biblioteca.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.bibliotecadeseries.data.model.Serie
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

@Composable
fun SerieCard(
    serie: Serie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorEstado(serie.estado),
            contentColor = BibliotecaColores.Tinta
        )
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = serie.titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = serie.estado.etiqueta(), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(serie.temporadasTotales) { indice ->
                    CuadroTemporada(lleno = indice < serie.temporadasVistas)
                }
            }
            Text(
                text = stringResource(
                    R.string.genero_avance,
                    serie.genero,
                    serie.temporadasVistas,
                    serie.temporadasTotales
                ),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CuadroTemporada(lleno: Boolean) {
    val forma = RoundedCornerShape(5.dp)
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(forma)
            .then(
                if (lleno) {
                    Modifier.background(BibliotecaColores.Tinta)
                } else {
                    Modifier.border(2.dp, BibliotecaColores.Tinta, forma)
                }
            )
    )
}

private fun colorEstado(estado: EstadoSerie): Color = when (estado) {
    EstadoSerie.POR_VER -> BibliotecaColores.AzulSuave
    EstadoSerie.VIENDO -> BibliotecaColores.NaranjaSuave
    EstadoSerie.TERMINADA -> BibliotecaColores.VerdeSuave
}

@Composable
private fun EstadoSerie.etiqueta(): String = stringResource(
    when (this) {
        EstadoSerie.POR_VER -> R.string.estado_por_ver
        EstadoSerie.VIENDO -> R.string.estado_viendo
        EstadoSerie.TERMINADA -> R.string.estado_terminada
    }
)