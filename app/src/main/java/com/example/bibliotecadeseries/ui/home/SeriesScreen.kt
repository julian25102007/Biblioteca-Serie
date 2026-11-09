package com.example.bibliotecadeseries.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.ui.components.SerieCard
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
                    containerColor = MaterialTheme.colorScheme.background
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
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(state.series, key = { it.id }) { serie ->
                            SerieCard(
                                serie = serie,
                                onClick = { onVerDetalle(serie.id) },
                                onEditar = { onEditar(serie.id) },
                                onEliminar = { onPedirEliminar(serie) }
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