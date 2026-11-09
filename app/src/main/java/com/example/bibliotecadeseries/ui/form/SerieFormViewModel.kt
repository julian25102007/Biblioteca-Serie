package com.example.bibliotecadeseries.ui.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bibliotecadeseries.appContainer
import com.example.bibliotecadeseries.ui.navigation.Routes
import com.example.bibliotecadeseries.domain.model.DatosSerie
import com.example.bibliotecadeseries.domain.repository.SeriesRepository
import com.example.bibliotecadeseries.domain.result.fold
import com.example.bibliotecadeseries.ui.toMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SerieFormViewModel(
    private val repository: SeriesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val serieId: Int? = savedStateHandle.get<Int>(Routes.ARG_ID)
        ?.takeIf { it != Routes.NEW_ID }

    private val _uiState = MutableStateFlow(SerieFormUiState(esEdicion = serieId != null))
    val uiState: StateFlow<SerieFormUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        cargarSerie()
    }


    fun cargarSerie() {
        val id = serieId ?: return
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorCarga = null) }
        loadJob = viewModelScope.launch {
            repository.getSerie(id).fold(
                onSuccess = { serie ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            titulo = serie.titulo,
                            genero = serie.genero,
                            plataforma = serie.plataforma,
                            temporadasTotales = serie.temporadasTotales.toString(),
                            temporadasVistas = serie.temporadasVistas.toString(),
                            favorita = serie.favorita
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorCarga = error.toMessage()) }
                }
            )
        }
    }

    fun onTituloChange(valor: String) {
        _uiState.update { it.copy(titulo = valor, errorTitulo = null) }
    }

    fun onGeneroChange(valor: String) {
        _uiState.update { it.copy(genero = valor, errorGenero = null) }
    }

    fun onPlataformaChange(valor: String) {
        _uiState.update { it.copy(plataforma = valor) }
    }

    fun onTemporadasTotalesChange(valor: String) {
        _uiState.update { it.copy(temporadasTotales = valor, errorTemporadasTotales = null) }
    }

    fun onTemporadasVistasChange(valor: String) {
        _uiState.update { it.copy(temporadasVistas = valor, errorTemporadasVistas = null) }
    }

    fun onFavoritaChange(valor: Boolean) {
        _uiState.update { it.copy(favorita = valor) }
    }

    fun onGuardar() {
        val actual = _uiState.value
        if (actual.isSaving) return

        val totales = actual.temporadasTotales.toIntOrNull()
        val vistas = actual.temporadasVistas.toIntOrNull()

        val errorTitulo = if (actual.titulo.isBlank()) "El título es obligatorio" else null
        val errorGenero = if (actual.genero.isBlank()) "El género es obligatorio" else null
        val errorTotales = if (totales == null || totales !in 1..MAX_TEMPORADAS) {
            "Escribe un número entre 1 y $MAX_TEMPORADAS"
        } else {
            null
        }
        val errorVistas = if (vistas == null || vistas < 0 || (totales != null && vistas > totales)) {
            "Debe ser un número entre 0 y el total de temporadas"
        } else {
            null
        }

        if (totales == null || vistas == null ||
            listOf(errorTitulo, errorGenero, errorTotales, errorVistas).any { it != null }
        ) {
            _uiState.update {
                it.copy(
                    errorTitulo = errorTitulo,
                    errorGenero = errorGenero,
                    errorTemporadasTotales = errorTotales,
                    errorTemporadasVistas = errorVistas
                )
            }
            return
        }

        val datos = DatosSerie(
            titulo = actual.titulo.trim(),
            genero = actual.genero.trim(),
            plataforma = actual.plataforma.trim(),
            temporadasTotales = totales,
            temporadasVistas = vistas,
            favorita = actual.favorita
        )

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val resultado = if (serieId == null) {
                repository.createSerie(datos)
            } else {
                repository.updateSerie(serieId, datos)
            }
            resultado.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            isSaving = false,
                            mensajeGuardado = if (serieId == null) "Serie creada" else "Serie actualizada"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { state -> state.copy(isSaving = false, userMessage = error.toMessage()) }
                }
            )
        }
    }


    fun onGuardadoConsumido() {
        _uiState.update { it.copy(mensajeGuardado = null) }
    }

    fun onMensajeMostrado() {
        _uiState.update { it.copy(userMessage = null) }
    }

    companion object {
        private const val MAX_TEMPORADAS = 50

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SerieFormViewModel(appContainer().seriesRepository, createSavedStateHandle())
            }
        }
    }
}