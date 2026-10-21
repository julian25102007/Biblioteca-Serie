package com.example.bibliotecadeseries.biblioteca.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
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
    onMas: () -> Unit,
    onMenos: () -> Unit,
    onEliminar: () -> Unit,
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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(
                        R.string.genero_avance,
                        serie.genero,
                        serie.temporadasVistas,
                        serie.temporadasTotales
                    ),
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                OutlinedIconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(44.dp),
                    border = BorderStroke(2.dp, BibliotecaColores.Tinta)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.descripcion_eliminar))
                }
                FilledIconButton(onClick = onMenos, modifier = Modifier.size(44.dp)) {
                    Text(text = stringResource(R.string.simbolo_menos), fontSize = 20.sp)
                }
                FilledIconButton(onClick = onMas, modifier = Modifier.size(44.dp)) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.descripcion_sumar))
                }
            }
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