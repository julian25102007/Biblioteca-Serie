package com.example.bibliotecadeseries.ui.form

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bibliotecadeseries.ui.components.BotonPrincipal
import com.example.bibliotecadeseries.ui.components.CampoTexto

@Composable
fun SerieFormScreen(
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    viewModel: SerieFormViewModel = viewModel(factory = SerieFormViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.mensajeGuardado) {
        state.mensajeGuardado?.let { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            onGuardado()
            viewModel.onGuardadoConsumido()
        }
    }

    SerieFormContent(
        state = state,
        onVolver = onVolver,
        onTituloChange = viewModel::onTituloChange,
        onGeneroChange = viewModel::onGeneroChange,
        onPlataformaChange = viewModel::onPlataformaChange,
        onTemporadasTotalesChange = viewModel::onTemporadasTotalesChange,
        onTemporadasVistasChange = viewModel::onTemporadasVistasChange,
        onFavoritaChange = viewModel::onFavoritaChange,
        onGuardar = viewModel::onGuardar,
        onReintentarCarga = viewModel::cargarSerie,
        onMensajeMostrado = viewModel::onMensajeMostrado
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieFormContent(
    state: SerieFormUiState,
    onVolver: () -> Unit,
    onTituloChange: (String) -> Unit,
    onGeneroChange: (String) -> Unit,
    onPlataformaChange: (String) -> Unit,
    onTemporadasTotalesChange: (String) -> Unit,
    onTemporadasVistasChange: (String) -> Unit,
    onFavoritaChange: (Boolean) -> Unit,
    onGuardar: () -> Unit,
    onReintentarCarga: () -> Unit,
    onMensajeMostrado: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            onMensajeMostrado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.esEdicion) "Editar serie" else "Nueva serie") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()

                state.errorCarga != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(state.errorCarga, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onReintentarCarga) { Text("Reintentar") }
                    }
                }

                else -> Formulario(
                    state = state,
                    onTituloChange = onTituloChange,
                    onGeneroChange = onGeneroChange,
                    onPlataformaChange = onPlataformaChange,
                    onTemporadasTotalesChange = onTemporadasTotalesChange,
                    onTemporadasVistasChange = onTemporadasVistasChange,
                    onFavoritaChange = onFavoritaChange,
                    onGuardar = onGuardar
                )
            }
        }
    }
}

@Composable
private fun Formulario(
    state: SerieFormUiState,
    onTituloChange: (String) -> Unit,
    onGeneroChange: (String) -> Unit,
    onPlataformaChange: (String) -> Unit,
    onTemporadasTotalesChange: (String) -> Unit,
    onTemporadasVistasChange: (String) -> Unit,
    onFavoritaChange: (Boolean) -> Unit,
    onGuardar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CampoTexto(
            value = state.titulo,
            onValueChange = onTituloChange,
            label = "Título",
            error = state.errorTitulo,
            enabled = !state.isSaving
        )
        CampoTexto(
            value = state.genero,
            onValueChange = onGeneroChange,
            label = "Género",
            error = state.errorGenero,
            enabled = !state.isSaving
        )
        CampoTexto(
            value = state.plataforma,
            onValueChange = onPlataformaChange,
            label = "Plataforma (opcional)",
            enabled = !state.isSaving
        )
        CampoTexto(
            value = state.temporadasTotales,
            onValueChange = onTemporadasTotalesChange,
            label = "Temporadas totales",
            error = state.errorTemporadasTotales,
            keyboardType = KeyboardType.Number,
            enabled = !state.isSaving
        )
        CampoTexto(
            value = state.temporadasVistas,
            onValueChange = onTemporadasVistasChange,
            label = "Temporadas vistas",
            error = state.errorTemporadasVistas,
            keyboardType = KeyboardType.Number,
            enabled = !state.isSaving
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Marcar como favorita", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = state.favorita,
                onCheckedChange = onFavoritaChange,
                enabled = !state.isSaving
            )
        }
        BotonPrincipal(
            texto = if (state.isSaving) "Guardando..." else "Guardar",
            onClick = onGuardar,
            enabled = !state.isSaving
        )
    }
}