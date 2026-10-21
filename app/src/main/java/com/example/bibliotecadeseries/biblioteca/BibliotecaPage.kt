package com.example.bibliotecadeseries.ui.biblioteca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bibliotecadeseries.R
import com.example.bibliotecadeseries.data.model.EstadoSerie
import com.example.bibliotecadeseries.ui.biblioteca.components.BotonPrincipal
import com.example.bibliotecadeseries.ui.biblioteca.components.EncabezadoFiltro
import com.example.bibliotecadeseries.ui.biblioteca.components.FiltrosEstado
import com.example.bibliotecadeseries.ui.biblioteca.components.ResumenCard
import com.example.bibliotecadeseries.ui.biblioteca.components.SerieCard

@Composable
fun BibliotecaPage(
    onAgregarSerie: () -> Unit,
    viewModel: BibliotecaViewModel = viewModel(factory = BibliotecaViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BibliotecaContent(
        state = state,
        onFiltroChange = viewModel::onFiltroChange,
        onTemporadaMas = viewModel::onTemporadaMas,
        onTemporadaMenos = viewModel::onTemporadaMenos,
        onEliminar = viewModel::onEliminar,
        onAgregarSerie = onAgregarSerie
    )
}

@Composable
private fun BibliotecaContent(
    state: BibliotecaUiState,
    onFiltroChange: (EstadoSerie?) -> Unit,
    onTemporadaMas: (Long) -> Unit,
    onTemporadaMenos: (Long) -> Unit,
    onEliminar: (Long) -> Unit,
    onAgregarSerie: () -> Unit
) {
    val filtroActivo = state.filtro

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
            if (filtroActivo == null) {
                ResumenCard(
                    avanceGeneral = state.avanceGeneral,
                    temporadasVistas = state.temporadasVistas,
                    temporadasTotales = state.temporadasTotales,
                    terminadas = state.terminadas,
                    total = state.total
                )
            } else {
                EncabezadoFiltro(estado = filtroActivo, cantidad = state.series.size)
            }
            FiltrosEstado(filtroActual = filtroActivo, onFiltroChange = onFiltroChange)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (state.series.isEmpty()) {
                    item { Text(text = stringResource(R.string.lista_vacia)) }
                } else {
                    items(state.series, key = { it.id }) { serie ->
                        SerieCard(
                            serie = serie,
                            onMas = { onTemporadaMas(serie.id) },
                            onMenos = { onTemporadaMenos(serie.id) },
                            onEliminar = { onEliminar(serie.id) }
                        )
                    }
                }
            }
            BotonPrincipal(
                texto = stringResource(R.string.boton_agregar),
                onClick = onAgregarSerie
            )
        }
    }
}