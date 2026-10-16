package com.example.bibliotecadeseries.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object BibliotecaColores {
    val Tinta = Color(0xFF1F2A44)
    val Crema = Color(0xFFFFF4D6)
    val CremaOscuro = Color(0xFFFFE7A3)
    val Coral = Color(0xFFFF6B4A)
    val Dorado = Color(0xFFFFC857)
    val PistaProgreso = Color(0xFF3B476B)
    val VerdeSuave = Color(0xFFBDE8C9)
    val NaranjaSuave = Color(0xFFFFD1A8)
    val AzulSuave = Color(0xFFCFD8FF)
    val Rojo = Color(0xFFB3261E)
}

private val EsquemaColores = lightColorScheme(
    primary = BibliotecaColores.Tinta,
    onPrimary = BibliotecaColores.Crema,
    secondary = BibliotecaColores.Coral,
    onSecondary = BibliotecaColores.Tinta,
    background = BibliotecaColores.Crema,
    onBackground = BibliotecaColores.Tinta,
    surface = Color.White,
    onSurface = BibliotecaColores.Tinta,
    error = BibliotecaColores.Rojo
)

@Composable
fun BibliotecaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaColores, content = content)
}