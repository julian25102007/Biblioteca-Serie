package com.example.bibliotecadeseries.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun LoginScreen(
    onSesionIniciada: () -> Unit,
    onIrARegistro: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.sesionIniciada) {
        if (state.sesionIniciada) {
            onSesionIniciada()
            viewModel.onSesionConsumida()
        }
    }

    LoginContent(
        state = state,
        onCorreoChange = viewModel::onCorreoChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        onIniciarSesion = viewModel::onIniciarSesion,
        onIrARegistro = onIrARegistro
    )
}


@Composable
fun LoginContent(
    state: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onIniciarSesion: () -> Unit,
    onIrARegistro: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Biblioteca de series", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
            Text("Inicia sesión para ver tu lista", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

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

            state.errorGeneral?.let { mensaje ->
                Text(mensaje, color = MaterialTheme.colorScheme.error)
            }

            BotonPrincipal(
                texto = if (state.isLoading) "Entrando..." else "Iniciar sesión",
                onClick = onIniciarSesion,
                enabled = !state.isLoading
            )
            TextButton(
                onClick = onIrARegistro,
                enabled = !state.isLoading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}