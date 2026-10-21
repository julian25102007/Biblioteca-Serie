package com.example.bibliotecadeseries.ui.biblioteca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bibliotecadeseries.BibliotecaApplication
import com.example.bibliotecadeseries.data.model.EstadoSerie
import com.example.bibliotecadeseries.data.model.Serie
import com.example.bibliotecadeseries.data.repository.BibliotecaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class BibliotecaViewModel(
    private val repository: BibliotecaRepository
) : ViewModel() {

    private val filtro = MutableStateFlow<EstadoSerie?>(null)

    private val _uiState = MutableStateFlow(BibliotecaUiState())
    val uiState: StateFlow<BibliotecaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.series, filtro) { series, filtroActual ->
                construirEstado(series, filtroActual)
            }.collect { nuevoEstado -> _uiState.value = nuevoEstado }
        }
    }

    private fun construirEstado(
        series: List<Serie>,
        filtroActual: EstadoSerie?
    ): BibliotecaUiState {
        val vistas = series.sumOf { it.temporadasVistas }
        val totales = series.sumOf { it.temporadasTotales }
        return BibliotecaUiState(
            series = if (filtroActual == null) series else series.filter { it.estado == filtroActual },
            filtro = filtroActual,
            total = series.size,
            terminadas = series.count { it.estado == EstadoSerie.TERMINADA },
            temporadasVistas = vistas,
            temporadasTotales = totales,
            avanceGeneral = if (totales == 0) 0 else vistas * 100 / totales
        )
    }

    fun onFiltroChange(nuevoFiltro: EstadoSerie?) {
        filtro.value = nuevoFiltro
    }

    fun onTemporadaMas(id: Long) {
        viewModelScope.launch { repository.cambiarVistas(id, 1) }
    }

    fun onTemporadaMenos(id: Long) {
        viewModelScope.launch { repository.cambiarVistas(id, -1) }
    }

    fun onEliminar(id: Long) {
        viewModelScope.launch { repository.eliminar(id) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplicacion = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BibliotecaApplication
                BibliotecaViewModel(aplicacion.container.bibliotecaRepository)
            }
        }
    }
}