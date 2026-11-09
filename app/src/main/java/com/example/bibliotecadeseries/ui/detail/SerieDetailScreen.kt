package com.example.bibliotecadeseries.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.ui.components.etiqueta

@Composable
fun SerieDetailScreen(
    onVolver: () -> Unit,
    onEditar: (Int) -> Unit,
    viewModel: SerieDetailViewModel = viewModel(factory = SerieDetailViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()


    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.cargarSerie() }

    SerieDetailContent(
        state = state,
        onVolver = onVolver,
        onEditar = onEditar,
        onReintentar = viewModel::cargarSerie
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieDetailContent(
    state: SerieDetailUiState,
    onVolver: () -> Unit,
    onEditar: (Int) -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la serie") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    state.serie?.let { serie ->
                        IconButton(onClick = { onEditar(serie.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar serie")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()

                state.serie != null -> DetalleSerie(state.serie)

                state.errorMessage != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onReintentar) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetalleSerie(serie: Serie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(serie.titulo, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        HorizontalDivider()
        FilaDetalle("Género", serie.genero)
        FilaDetalle("Plataforma", serie.plataforma.ifBlank { "—" })
        FilaDetalle("Temporadas totales", serie.temporadasTotales.toString())
        FilaDetalle("Temporadas vistas", serie.temporadasVistas.toString())
        FilaDetalle("Estado", serie.estado.etiqueta())
        FilaDetalle("Avance", "${(serie.avance * 100).toInt()} %")
        FilaDetalle("Favorita", if (serie.favorita) "Sí" else "No")
    }
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(etiqueta, style = MaterialTheme.typography.labelMedium)
        Text(valor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}