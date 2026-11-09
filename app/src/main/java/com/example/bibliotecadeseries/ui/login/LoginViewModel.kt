package com.example.bibliotecadeseries.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bibliotecadeseries.appContainer
import com.example.bibliotecadeseries.domain.repository.AuthRepository
import com.example.bibliotecadeseries.domain.result.fold
import com.example.bibliotecadeseries.ui.toMessage
import com.example.bibliotecadeseries.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCorreoChange(valor: String) {
        _uiState.update { it.copy(correo = valor, errorCorreo = null, errorGeneral = null) }
    }

    fun onContrasenaChange(valor: String) {
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null, errorGeneral = null) }
    }

    fun onIniciarSesion() {
        val actual = _uiState.value
        if (actual.isLoading) return

        val errorCorreo = when {
            actual.correo.isBlank() -> "El correo es obligatorio"
            !actual.correo.isValidEmail() -> "Escribe un correo válido"
            else -> null
        }
        val errorContrasena = if (actual.contrasena.isEmpty()) "La contraseña es obligatoria" else null

        if (errorCorreo != null || errorContrasena != null) {
            _uiState.update { it.copy(errorCorreo = errorCorreo, errorContrasena = errorContrasena) }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }
        viewModelScope.launch {
            authRepository.login(actual.correo.trim(), actual.contrasena).fold(
                onSuccess = {
                    _uiState.update { state -> state.copy(isLoading = false, sesionIniciada = true) }
                },
                onFailure = { error ->
                    _uiState.update { state ->
                        state.copy(isLoading = false, errorGeneral = error.toMessage())
                    }
                }
            )
        }
    }


    fun onSesionConsumida() {
        _uiState.update { it.copy(sesionIniciada = false) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { LoginViewModel(appContainer().authRepository) }
        }
    }
}