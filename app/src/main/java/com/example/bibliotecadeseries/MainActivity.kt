package com.example.bibliotecadeseries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bibliotecadeseries.biblioteca.BibliotecaPage
import com.example.bibliotecadeseries.ui.theme.BibliotecaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliotecaTheme {
                BibliotecaPage()
            }
        }
    }
}