package com.example.bibliotecadeseries.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bibliotecadeseries.appContainer
import com.example.bibliotecadeseries.domain.repository.SeriesRepository
import com.example.bibliotecadeseries.domain.result.fold
import com.example.bibliotecadeseries.ui.navigation.Routes
import com.example.bibliotecadeseries.ui.toMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SerieDetailViewModel(
    private val repository: SeriesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val serieId: Int = checkNotNull(savedStateHandle.get<Int>(Routes.ARG_ID))

    private val _uiState = MutableStateFlow(SerieDetailUiState())
    val uiState: StateFlow<SerieDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun cargarSerie() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = it.serie == null, errorMessage = null) }
        loadJob = viewModelScope.launch {
            repository.getSerie(serieId).fold(
                onSuccess = { serie ->
                    _uiState.update { it.copy(isLoading = false, serie = serie) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.toMessage()) }
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SerieDetailViewModel(appContainer().seriesRepository, createSavedStateHandle())
            }
        }
    }
}