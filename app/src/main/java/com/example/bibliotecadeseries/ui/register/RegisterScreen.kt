package com.example.bibliotecadeseries.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bibliotecadeseries.ui.components.BotonPrincipal
import com.example.bibliotecadeseries.ui.components.CampoTexto

@Composable
fun RegisterScreen(
    onRegistroExitoso: () -> Unit,
    onVolver: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.registroExitoso) {
        if (state.registroExitoso) {
            onRegistroExitoso()
            viewModel.onRegistroConsumido()
        }
    }

    RegisterContent(
        state = state,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        onConfirmacionChange = viewModel::onConfirmacionChange,
        onRegistrar = viewModel::onRegistrar,
        onVolver = onVolver
    )
}

@Composable
fun RegisterContent(
    state: RegisterUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmacionChange: (String) -> Unit,
    onRegistrar: () -> Unit,
    onVolver: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Crear cuenta", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)

            CampoTexto(
                value = state.nombre,
                onValueChange = onNombreChange,
                label = "Nombre",
                error = state.errorNombre,
                enabled = !state.isLoading
            )
            CampoTexto(
                value = state.correo,
                onValueChange = onCorreoChange,
                label = "Correo",
                error = state.errorCorreo,
                keyboardType = KeyboardType.Email,
                enabled = !state.isLoading
            )
            CampoTexto(
                value = state.contrasena,
                onValueChange = onContrasenaChange,
                label = "Contraseña",
                error = state.errorContrasena,
                esContrasena = true,
                enabled = !state.isLoading
            )
            CampoTexto(
                value = state.confirmacion,
                onValueChange = onConfirmacionChange,
                label = "Confirmar contraseña",
                error = state.errorConfirmacion,
                esContrasena = true,
                enabled = !state.isLoading
            )

            state.errorGeneral?.let { mensaje ->
                Text(mensaje, color = MaterialTheme.colorScheme.error)
            }

            BotonPrincipal(
                texto = if (state.isLoading) "Creando cuenta..." else "Registrarme",
                onClick = onRegistrar,
                enabled = !state.isLoading
            )
            TextButton(
                onClick = onVolver,
                enabled = !state.isLoading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}