package com.example.bibliotecadeseries.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bibliotecadeseries.appContainer
import com.example.bibliotecadeseries.domain.repository.AuthRepository
import com.example.bibliotecadeseries.domain.result.fold
import com.example.bibliotecadeseries.ui.toMessage
import com.example.bibliotecadeseries.util.MIN_PASSWORD_LENGTH
import com.example.bibliotecadeseries.util.isValidEmail
import com.example.bibliotecadeseries.util.isValidPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNombreChange(valor: String) {
        _uiState.update { it.copy(nombre = valor, errorNombre = null, errorGeneral = null) }
    }

    fun onCorreoChange(valor: String) {
        _uiState.update { it.copy(correo = valor, errorCorreo = null, errorGeneral = null) }
    }

    fun onContrasenaChange(valor: String) {
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null, errorGeneral = null) }
    }

    fun onConfirmacionChange(valor: String) {
        _uiState.update { it.copy(confirmacion = valor, errorConfirmacion = null, errorGeneral = null) }
    }

    fun onRegistrar() {
        val actual = _uiState.value
        if (actual.isLoading) return

        val errorNombre = if (actual.nombre.isBlank()) "El nombre es obligatorio" else null
        val errorCorreo = when {
            actual.correo.isBlank() -> "El correo es obligatorio"
            !actual.correo.isValidEmail() -> "Escribe un correo válido"
            else -> null
        }
        val errorContrasena = if (!actual.contrasena.isValidPassword()) {
            "Mínimo $MIN_PASSWORD_LENGTH caracteres"
        } else {
            null
        }
        val errorConfirmacion = if (actual.confirmacion != actual.contrasena) {
            "Las contraseñas no coinciden"
        } else {
            null
        }

        if (listOf(errorNombre, errorCorreo, errorContrasena, errorConfirmacion).any { it != null }) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorCorreo = errorCorreo,
                    errorContrasena = errorContrasena,
                    errorConfirmacion = errorConfirmacion
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }
        viewModelScope.launch {
            authRepository.register(actual.nombre.trim(), actual.correo.trim(), actual.contrasena).fold(
                onSuccess = {
                    _uiState.update { state -> state.copy(isLoading = false, registroExitoso = true) }
                },
                onFailure = { error ->
                    _uiState.update { state ->
                        state.copy(isLoading = false, errorGeneral = error.toMessage())
                    }
                }
            )
        }
    }


    fun onRegistroConsumido() {
        _uiState.update { it.copy(registroExitoso = false) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { RegisterViewModel(appContainer().authRepository) }
        }
    }
}