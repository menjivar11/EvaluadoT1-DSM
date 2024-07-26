package com.example.listaprueba1

// Clase de datos para representar una tarea en la app
data class Tarea(
    val id: Int,             // Identificador único para la tarea
    val name: String,        // Nombre o descripción de la tarea
    var isDone: Boolean      // Estado que indica si la tarea está completa o no
)
