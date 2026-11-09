package com.example.bibliotecadeseries.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bibliotecadeseries.appContainer
import com.example.bibliotecadeseries.domain.model.Serie
import com.example.bibliotecadeseries.domain.repository.AuthRepository
import com.example.bibliotecadeseries.domain.repository.SeriesRepository
import com.example.bibliotecadeseries.domain.result.AppError
import com.example.bibliotecadeseries.domain.result.fold
import com.example.bibliotecadeseries.ui.toMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeriesViewModel(
    private val seriesRepository: SeriesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeriesUiState())
    val uiState: StateFlow<SeriesUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null


    fun cargarSeries() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = it.series.isEmpty(), errorMessage = null) }
        loadJob = viewModelScope.launch {
            seriesRepository.getSeries().fold(
                onSuccess = { series ->
                    _uiState.update { it.copy(isLoading = false, series = series) }
                },
                onFailure = { error -> mostrarError(error) }
            )
        }
    }


    private fun mostrarError(error: AppError) {
        val mensaje = error.toMessage()
        _uiState.update { state ->
            if (state.series.isEmpty()) {
                state.copy(isLoading = false, errorMessage = mensaje)
            } else {
                state.copy(isLoading = false, userMessage = mensaje)
            }
        }
    }

    fun onPedirEliminar(serie: Serie) {
        _uiState.update { it.copy(serieAEliminar = serie) }
    }

    fun onCancelarEliminar() {
        _uiState.update { it.copy(serieAEliminar = null) }
    }

    fun onConfirmarEliminar() {
        val serie = _uiState.value.serieAEliminar ?: return
        _uiState.update { it.copy(serieAEliminar = null) }
        viewModelScope.launch {
            seriesRepository.deleteSerie(serie.id).fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            series = state.series.filterNot { it.id == serie.id },
                            userMessage = "Serie \"${serie.titulo}\" eliminada"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(userMessage = error.toMessage()) }
                }
            )
        }
    }

    fun onCerrarSesion() {
        authRepository.logout()
        _uiState.update { it.copy(sesionCerrada = true) }
    }


    fun onSesionConsumida() {
        _uiState.update { it.copy(sesionCerrada = false) }
    }

    fun onMensajeMostrado() {
        _uiState.update { it.copy(userMessage = null) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = appContainer()
                SeriesViewModel(container.seriesRepository, container.authRepository)
            }
        }
    }
}