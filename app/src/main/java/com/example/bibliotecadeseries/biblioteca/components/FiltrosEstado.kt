package com.example.bibliotecadeseries.ui.biblioteca.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecadeseries.R
import com.example.bibliotecadeseries.data.model.EstadoSerie

@Composable
fun EncabezadoFiltro(
    estado: EstadoSerie,
    cantidad: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = stringResource(R.string.filtro_activo), fontSize = 14.sp)
        Text(
            text = stringResource(
                R.string.filtro_titulo,
                estado.etiqueta(),
                pluralStringResource(R.plurals.series_cantidad, cantidad, cantidad)
            ),
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}