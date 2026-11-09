package com.example.bibliotecadeseries.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

@Composable
fun SerieCard(
    serie: Serie,
    onClick: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(20.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .clickable(onClick = onClick),
        shape = forma,
        colors = CardDefaults.cardColors(
            containerColor = serie.estado.color(),
            contentColor = BibliotecaColores.Tinta
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = serie.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (serie.favorita) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorita",
                        tint = BibliotecaColores.Coral
                    )
                }
                Text(serie.estado.etiqueta(), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            LinearProgressIndicator(
                progress = { serie.avance },
                modifier = Modifier.fillMaxWidth(),
                color = BibliotecaColores.Tinta,
                trackColor = Color.White
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${serie.genero} · ${serie.temporadasVistas} de ${serie.temporadasTotales} temporadas",
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar serie")
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar serie")
                }
            }
        }
    }
}