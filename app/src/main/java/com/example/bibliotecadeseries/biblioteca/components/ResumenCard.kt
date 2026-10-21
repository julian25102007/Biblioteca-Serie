package com.example.bibliotecadeseries.biblioteca.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecadeseries.R
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

@Composable
fun ResumenCard(
    avanceGeneral: Int,
    temporadasVistas: Int,
    temporadasTotales: Int,
    terminadas: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = BibliotecaColores.Tinta,
            contentColor = BibliotecaColores.Crema
        )
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = stringResource(R.string.app_titulo), fontSize = 14.sp)
            Text(
                text = stringResource(R.string.avance_porcentaje, avanceGeneral),
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold
            )
            LinearProgressIndicator(
                progress = { avanceGeneral / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = BibliotecaColores.Dorado,
                trackColor = BibliotecaColores.PistaProgreso
            )
            Text(
                text = stringResource(R.string.resumen, temporadasVistas, temporadasTotales, terminadas, total),
                fontSize = 12.sp
            )
        }
    }
}