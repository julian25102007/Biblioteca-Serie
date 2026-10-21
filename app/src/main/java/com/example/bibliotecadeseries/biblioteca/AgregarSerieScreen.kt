package com.example.bibliotecadeseries.biblioteca

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecadeseries.R
import com.example.bibliotecadeseries.biblioteca.components.BotonPrincipal
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores

@Composable
fun AgregarSerieScreen(
    formulario: FormularioSerie,
    hayError: Boolean,
    onTituloChange: (String) -> Unit,
    onGeneroChange: (String) -> Unit,
    onTemporadasChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    BackHandler(onBack = onCancelar)

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        errorContainerColor = Color.White
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.nueva_serie),
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold
            )
            OutlinedTextField(
                value = formulario.titulo,
                onValueChange = onTituloChange,
                label = { Text(stringResource(R.string.campo_titulo)) },
                isError = hayError,
                supportingText = { if (hayError) Text(stringResource(R.string.error_formulario)) },
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = formulario.genero,
                onValueChange = onGeneroChange,
                label = { Text(stringResource(R.string.campo_genero)) },
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = formulario.temporadas,
                onValueChange = onTemporadasChange,
                label = { Text(stringResource(R.string.campo_temporadas)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = hayError,
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BibliotecaColores.CremaOscuro,
                    contentColor = BibliotecaColores.Tinta
                )
            ) {
                Text(
                    text = stringResource(R.string.info_guardado),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(14.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            BotonPrincipal(texto = stringResource(R.string.boton_guardar), onClick = onGuardar)
            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, BibliotecaColores.Tinta)
            ) {
                Text(
                    text = stringResource(R.string.boton_cancelar),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}