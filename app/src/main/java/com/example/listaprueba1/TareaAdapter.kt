package com.example.listaprueba1

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adaptador para la lista de tareas en un RecyclerView
class TareaAdapter(
    private val tasks: List<Tarea>,                // Lista de tareas que se mostrarán en el RecyclerView
    private val onDelete: (Tarea) -> Unit,          // Función para cuando se elimine una tarea
    private val onToggle: (Tarea) -> Unit           // Función para cuando se cambie el estado de una tarea
) : RecyclerView.Adapter<TareaAdapter.TaskViewHolder>() {

    // ViewHolder que mantiene las vistas de cada item en la lista
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.taskName)        // Referencia al TextView que muestra el nombre de la tarea
        val deleteButton: View = view.findViewById(R.id.deleteButton)    // Referencia al botón de eliminar tarea
        val taskCheckBox: CheckBox = view.findViewById(R.id.taskCheckBox) // Referencia al CheckBox para marcar la tarea como hecha
    }

    // Crea una nueva vista para un item del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea, parent, false)
        return TaskViewHolder(view)
    }

    // Asocia datos con la vista del ViewHolder en una posición específica
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position] // Obtiene la tarea en la posición actual
        holder.taskName.text = task.name // Establece el nombre de la tarea en el TextView
        holder.taskCheckBox.isChecked = task.isDone // Marca el CheckBox si la tarea está completada

        // Aplica o quita el tachado del texto según el estado de la tarea
        holder.taskName.paintFlags = if (task.isDone) {
            holder.taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // Aplica el tachado si la tarea está hecha
        } else {
            holder.taskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // Quita el tachado si la tarea no está hecha
        }

        // Configura el listener para el botón de eliminar
        holder.deleteButton.setOnClickListener { onDelete(task) }

        // Configura el listener para el CheckBox
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            // Crea una nueva instancia de `Tarea` con el estado actualizado
            val updatedTask = task.copy(isDone = isChecked)
            onToggle(updatedTask) // Llama a la función para manejar el cambio de estado
        }
    }

    // Devuelve el número total de items en la lista
    override fun getItemCount(): Int = tasks.size
}
