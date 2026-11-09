package com.example.bibliotecadeseries.ui

import com.example.bibliotecadeseries.domain.result.AppError


fun AppError.toMessage(): String = when (this) {
    AppError.Network -> "No hay conexión con el servidor. Revisa tu red"
    AppError.Unauthorized -> "Correo o contraseña incorrectos, o la sesión expiró"
    is AppError.Http -> detail ?: when (code) {
        404 -> "No se encontró el registro"
        409 -> "Ese correo ya está registrado"
        422 -> "Los datos enviados no son válidos"
        else -> "Error del servidor ($code)"
    }
    is AppError.Unknown -> "Ocurrió un error inesperado"
}