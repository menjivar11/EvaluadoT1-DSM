package com.example.listaprueba1

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TareaRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Guarda la lista de tareas en SharedPreferences como un JSON
    fun saveTasks(tasks: List<Tarea>) {
        val json = gson.toJson(tasks)
        sharedPreferences.edit().putString("task_list", json).apply()
    }

    // Carga la lista de tareas desde SharedPreferences
    fun loadTasks(): MutableList<Tarea> {
        val json = sharedPreferences.getString("task_list", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Tarea>>() {}.type
        return gson.fromJson(json, type)
    }
}
