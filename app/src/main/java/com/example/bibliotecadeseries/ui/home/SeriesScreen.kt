package com.example.bibliotecadeseries.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bibliotecadeseries.domain.model.EstadoSerie
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.ui.components.SerieCard
import com.example.bibliotecadeseries.ui.components.paleta
import com.example.bibliotecadeseries.ui.theme.BibliotecaColores


@Composable
fun SeriesScreen(
    onNuevaSerie: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onEditar: (Int) -> Unit,
    onSesionCerrada: () -> Unit,
    viewModel: SeriesViewModel = viewModel(factory = SeriesViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()


    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.cargarSeries() }

    LaunchedEffect(state.sesionCerrada) {
        if (state.sesionCerrada) {
            onSesionCerrada()
            viewModel.onSesionConsumida()
        }
    }

    SeriesContent(
        state = state,
        onReintentar = viewModel::cargarSeries,
        onNuevaSerie = onNuevaSerie,
        onVerDetalle = onVerDetalle,
        onEditar = onEditar,
        onSumarTemporada = { viewModel.onCambiarTemporadas(it, +1) },
        onRestarTemporada = { viewModel.onCambiarTemporadas(it, -1) },
        onPedirEliminar = viewModel::onPedirEliminar,
        onConfirmarEliminar = viewModel::onConfirmarEliminar,
        onCancelarEliminar = viewModel::onCancelarEliminar,
        onCerrarSesion = viewModel::onCerrarSesion,
        onMensajeMostrado = viewModel::onMensajeMostrado
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesContent(
    state: SeriesUiState,
    onReintentar: () -> Unit,
    onNuevaSerie: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onEditar: (Int) -> Unit,
    onSumarTemporada: (Int) -> Unit,
    onRestarTemporada: (Int) -> Unit,
    onPedirEliminar: (Serie) -> Unit,
    onConfirmarEliminar: () -> Unit,
    onCancelarEliminar: () -> Unit,
    onCerrarSesion: () -> Unit,
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
                title = { Text("Mis series", fontWeight = FontWeight.ExtraBold) },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BibliotecaColores.Dorado,
                    titleContentColor = BibliotecaColores.Tinta,
                    actionIconContentColor = BibliotecaColores.Tinta
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaSerie,
                containerColor = BibliotecaColores.Coral,
                contentColor = BibliotecaColores.Tinta
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar serie")
            }
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

                state.errorMessage != null && state.series.isEmpty() -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onReintentar) { Text("Reintentar") }
                    }
                }

                state.series.isEmpty() -> {
                    Text(
                        text = "Aún no tienes series.\nToca + para agregar la primera.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item { ResumenSeries(state.series) }

                        items(state.series, key = { it.id }) { serie ->
                            SerieCard(
                                serie = serie,
                                onClick = { onVerDetalle(serie.id) },
                                onEditar = { onEditar(serie.id) },
                                onEliminar = { onPedirEliminar(serie) },
                                onSumarTemporada = { onSumarTemporada(serie.id) },
                                onRestarTemporada = { onRestarTemporada(serie.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    state.serieAEliminar?.let { serie ->
        AlertDialog(
            onDismissRequest = onCancelarEliminar,
            title = { Text("Eliminar serie") },
            text = { Text("¿Seguro que quieres eliminar \"${serie.titulo}\"? Esta acción no se puede deshacer.") },
            confirmButton = { TextButton(onClick = onConfirmarEliminar) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = onCancelarEliminar) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun ResumenSeries(series: List<Serie>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChipResumen(
            "Por ver", series.count { it.estado == EstadoSerie.POR_VER },
            EstadoSerie.POR_VER.paleta().acento, Modifier.weight(1f)
        )
        ChipResumen(
            "Viendo", series.count { it.estado == EstadoSerie.VIENDO },
            EstadoSerie.VIENDO.paleta().acento, Modifier.weight(1f)
        )
        ChipResumen(
            "Terminadas", series.count { it.estado == EstadoSerie.TERMINADA },
            EstadoSerie.TERMINADA.paleta().acento, Modifier.weight(1f)
        )
    }
}

@Composable
private fun ChipResumen(etiqueta: String, cantidad: Int, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$cantidad", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text(etiqueta, color = Color.White, fontSize = 12.sp)
    }
}