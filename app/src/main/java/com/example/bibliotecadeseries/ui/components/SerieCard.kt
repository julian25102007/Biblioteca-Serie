package com.example.bibliotecadeseries.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieCard(
    serie: Serie,
    onClick: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onSumarTemporada: () -> Unit,
    onRestarTemporada: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(24.dp)
    val paleta = serie.estado.paleta()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .clickable(onClick = onClick),
        shape = forma,
        colors = CardDefaults.cardColors(
            containerColor = paleta.inicio,
            contentColor = BibliotecaColores.Tinta
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(paleta.inicio, paleta.fin)))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = serie.titulo,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
                if (serie.favorita) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorita",
                        tint = BibliotecaColores.Coral
                    )
                }
                Text(
                    text = serie.estado.etiqueta(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(paleta.acento)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = listOf(serie.genero, serie.plataforma)
                        .filter { it.isNotBlank() }
                        .joinToString(" · "),
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEditar, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar serie",
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onEliminar, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar serie",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { serie.avance },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = paleta.acento,
                trackColor = Color.White.copy(alpha = 0.75f),
                gapSize = 0.dp,
                drawStopIndicator = {}
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(
                    onClick = onRestarTemporada,
                    enabled = serie.temporadasVistas > 0,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.85f),
                        contentColor = BibliotecaColores.Tinta,
                        disabledContainerColor = Color.White.copy(alpha = 0.4f),
                        disabledContentColor = BibliotecaColores.Tinta.copy(alpha = 0.35f)
                    )
                ) {
                    Text("−", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "${serie.temporadasVistas} de ${serie.temporadasTotales} temporadas",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = onSumarTemporada,
                    enabled = serie.temporadasVistas < serie.temporadasTotales,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = paleta.acento,
                        contentColor = Color.White,
                        disabledContainerColor = Color.White.copy(alpha = 0.4f),
                        disabledContentColor = BibliotecaColores.Tinta.copy(alpha = 0.35f)
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Vista", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}