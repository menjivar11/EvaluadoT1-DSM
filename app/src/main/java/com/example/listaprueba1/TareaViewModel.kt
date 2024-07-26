package com.example.listaprueba1

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.runtime.mutableStateListOf

class TareaViewModel(application: Application) : AndroidViewModel(application) {
    // Lista mutable de tareas que se observa en la UI
    private val _tasks = mutableStateListOf<Tarea>()
    val tasks: List<Tarea> get() = _tasks // Exposición de la lista de tareas como una lista inmutable

    // SharedPreferences para guardar y cargar las tareas
    private val sharedPreferences = application.getSharedPreferences("tasks_prefs", Context.MODE_PRIVATE)
    private val gson = Gson() // Instancia de Gson para conversión entre objetos y JSON

    // Inicializa el ViewModel cargando las tareas desde SharedPreferences
    init {
        loadTasks()
    }

    // Variable para generar IDs únicos para nuevas tareas
    private var nextId = 1

    // Añade una nueva tarea a la lista y la guarda en SharedPreferences
    fun addTask(name: String) {
        val task = Tarea(nextId++, name, false) // Crea una nueva tarea con un ID único
        _tasks.add(task) // Añade la tarea a la lista mutable
        saveTasks() // Guarda la lista actualizada en SharedPreferences
    }

    // Elimina una tarea de la lista y actualiza SharedPreferences
    fun removeTask(task: Tarea) {
        _tasks.remove(task) // Elimina la tarea de la lista mutable
        saveTasks() // Guarda la lista actualizada en SharedPreferences
    }

    // Actualiza el estado de una tarea (completada o no) y guarda los cambios
    fun updateTask(id: Int, isDone: Boolean) {
        _tasks.find { it.id == id }?.isDone = isDone // Encuentra la tarea por ID y actualiza su estado
        saveTasks() // Guarda la lista actualizada en SharedPreferences
    }

    // Guarda la lista de tareas en SharedPreferences como JSON
    private fun saveTasks() {
        val tasksJson = gson.toJson(_tasks) // Convierte la lista de tareas a JSON
        sharedPreferences.edit().putString("tasks", tasksJson).apply() // Guarda el JSON en SharedPreferences
    }

    // Carga la lista de tareas desde SharedPreferences
    private fun loadTasks() {
        val tasksJson = sharedPreferences.getString("tasks", null) // Obtiene el JSON desde SharedPreferences
        if (tasksJson != null) {
            // Convierte el JSON de vuelta a una lista de tareas
            val taskType = object : TypeToken<MutableList<Tarea>>() {}.type
            _tasks.addAll(gson.fromJson(tasksJson, taskType)) // Añade las tareas cargadas a la lista mutable
            nextId = (_tasks.maxOfOrNull { it.id } ?: 0) + 1 // Ajusta el próximo ID para nuevas tareas
        }
    }
}
