package com.example.bibliotecadeseries.ui.login

data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorGeneral: String? = null,
    val isLoading: Boolean = false,
    val sesionIniciada: Boolean = false
)