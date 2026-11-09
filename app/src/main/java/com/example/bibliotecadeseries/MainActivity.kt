package com.example.bibliotecadeseries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bibliotecadeseries.ui.navigation.AppNavHost
import com.example.bibliotecadeseries.ui.navigation.Routes
import com.example.bibliotecadeseries.ui.theme.BibliotecaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as BibliotecaApplication).container
        val startDestination =
            if (container.authRepository.isLoggedIn()) Routes.HOME else Routes.LOGIN

        setContent {
            BibliotecaTheme {
                AppNavHost(
                    startDestination = startDestination,
                    sessionExpired = container.sessionExpired
                )
            }
        }
    }
}