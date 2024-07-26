package com.example.listaprueba1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.listaprueba1.ui.theme.ListaPrueba1Theme

class MainActivity : ComponentActivity() {
    // Aqui se obtiene la instancia del ViewModel
    private val tareaViewModel: TareaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura el contenido de la actividad utilizando Jetpack Compose
        setContent {
            // Aplica el tema definido para la aplicación
            ListaPrueba1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama a la función composable que renderiza la pantalla de tareas
                    TaskListScreen(tareaViewModel = tareaViewModel)
                }
            }
        }
    }
}

// Con esto se muestra la pantalla de la lista de tareas y permite añadir nuevas tareas
@Composable
fun TaskListScreen(tareaViewModel: TareaViewModel) {
    // Observa el estado de las tareas desde el ViewModel
    val tasks by remember { mutableStateOf(tareaViewModel.tasks) }
    // Estado para el nombre de la nueva tarea
    var newTaskName by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Fila que contiene el campo de texto y el botón para añadir tareas
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    // Añade una nueva tarea cuando se presiona "Ok" en el teclado
                    if (newTaskName.isNotEmpty()) {
                        tareaViewModel.addTask(newTaskName)
                        newTaskName = ""
                    }
                })
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                // Añade una nueva tarea al hacer clic en el botón "Add"
                if (newTaskName.isNotEmpty()) {
                    tareaViewModel.addTask(newTaskName)
                    newTaskName = ""
                }
            }) {
                Text("Add")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Lista de tareas utilizando LazyColumn
        LazyColumn {
            items(tasks, key = { it.id }) { tarea ->
                // Muestra cada tarea usando la función composable TaskItem
                TaskItem(
                    tarea = tarea,
                    onDelete = { tareaViewModel.removeTask(it) },
                    onToggle = { id, isDone ->
                        tareaViewModel.updateTask(id, isDone)
                    }
                )
            }
        }
    }
}

// Aqui se muestra cada tarea individual
@Composable
fun TaskItem(tarea: Tarea, onDelete: (Tarea) -> Unit, onToggle: (Int, Boolean) -> Unit) {
    // Estado para si la tarea está marcada como completada
    var isChecked by remember { mutableStateOf(tarea.isDone) }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        // Checkbox para marcar o desmarcar la tarea
        Checkbox(
            checked = isChecked,
            onCheckedChange = { newChecked ->
                isChecked = newChecked
                onToggle(tarea.id, newChecked) // Notifica al ViewModel sobre el cambio de estado
            }
        )
        Text(
            text = tarea.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = if (isChecked) {
                // Aplica el estilo de texto tachado si la tarea está completada
                LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
            } else {
                LocalTextStyle.current
            }
        )
        // Botón para eliminar la tarea
        Button(onClick = { onDelete(tarea) }) {
            Text("Delete")
        }
    }
}
