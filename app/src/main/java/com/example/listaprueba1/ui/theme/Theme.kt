package com.example.listaprueba1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Definir una paleta de colores para el tema
private val LightColorScheme = lightColorScheme(
    primary = Purple200,
    secondary = Teal200
    // Puedes añadir más colores si es necesario
)

@Composable
fun ListaPrueba1Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
