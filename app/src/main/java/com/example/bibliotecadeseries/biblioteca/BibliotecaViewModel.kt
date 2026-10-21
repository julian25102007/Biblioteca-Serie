package com.example.bibliotecadeseries.biblioteca

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BibliotecaViewModel(
    private val repository: BibliotecaRepository
) : ViewModel() {

    private val filtro = MutableStateFlow<EstadoSerie?>(null)
    private val pantalla = MutableStateFlow(Pantalla.LISTA)
    private val formulario = MutableStateFlow(FormularioSerie())
    private val errorFormulario = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(BibliotecaUiState())
    val uiState: StateFlow<BibliotecaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.series,
                filtro,
                pantalla,
                formulario,
                errorFormulario
            ) { series, filtroActual, pantallaActual, form, hayError ->
                construirEstado(series, filtroActual, pantallaActual, form, hayError)
            }.collect { nuevoEstado -> _uiState.value = nuevoEstado }
        }
    }

    private fun construirEstado(
        series: List<Serie>,
        filtroActual: EstadoSerie?,
        pantallaActual: Pantalla,
        form: FormularioSerie,
        hayError: Boolean
    ): BibliotecaUiState {
        val vistas = series.sumOf { it.temporadasVistas }
        val totales = series.sumOf { it.temporadasTotales }
        return BibliotecaUiState(
            series = if (filtroActual == null) series else series.filter { it.estado == filtroActual },
            filtro = filtroActual,
            pantalla = pantallaActual,
            formulario = form,
            errorFormulario = hayError,
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

    fun onAbrirFormulario() {
        pantalla.value = Pantalla.AGREGAR
    }

    fun onTituloChange(valor: String) {
        formulario.update { it.copy(titulo = valor) }
        errorFormulario.value = false
    }

    fun onGeneroChange(valor: String) {
        formulario.update { it.copy(genero = valor) }
    }

    fun onTemporadasChange(valor: String) {
        formulario.update { it.copy(temporadas = valor) }
        errorFormulario.value = false
    }

    fun onCancelar() {
        formulario.value = FormularioSerie()
        errorFormulario.value = false
        pantalla.value = Pantalla.LISTA
    }

    fun onAgregar() {
        val datos = formulario.value
        val temporadas = datos.temporadas.toIntOrNull()
        if (datos.titulo.isBlank() || temporadas == null || temporadas <= 0) {
            errorFormulario.value = true
            return
        }
        viewModelScope.launch {
            repository.agregar(
                Serie(
                    id = System.currentTimeMillis(),
                    titulo = datos.titulo.trim(),
                    genero = datos.genero.trim(),
                    temporadasTotales = temporadas
                )
            )
            onCancelar()
        }
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