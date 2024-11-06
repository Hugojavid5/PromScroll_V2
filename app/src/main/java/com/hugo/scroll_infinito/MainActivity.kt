package com.hugo.scroll_infinito

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * MainActivity es la actividad principal de la aplicación, que permite a los usuarios
 * añadir y eliminar tareas de una lista.
 */
class MainActivity : ComponentActivity() {

    // Declaración de variables
    private lateinit var botonAceptar: Button
    private lateinit var textoEmail: EditText
    private lateinit var rvTareas: RecyclerView
    private var tareas = mutableListOf<String>()
    private lateinit var adaptador: TareaAdaptador
    private lateinit var mediaPlayer: MediaPlayer
    var tasks = mutableListOf<Task>()

    /**
     * Método llamado cuando se crea la actividad.
     * Inicializa la interfaz de usuario y el MediaPlayer.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()

        // Inicializa el MediaPlayer con el archivo de sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_aniadir)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_eliminar)
    }

    /**
     * Inicializa la interfaz de usuario, incluyendo vistas, oyentes y el RecyclerView.
     */
    private fun initUI() {
        initView()
        initRecyclerView()
    }

    /**
     * Añade una nueva tarea a la lista y notifica al adaptador sobre el cambio.
     *
     * @param newTask La tarea que se va a añadir.
     */
    private fun addTask() {
        val taskToAdd = textoEmail.text.toString().trim()
        if (taskToAdd.isNotEmpty()) { // Verificar que el campo no esté vacío
            val dbHelper = (application as TaskApplication).dbHelper
            val taskId = dbHelper.addTarea(taskToAdd) // Inserta en la base de datos
            val newTask = Task(id = taskId, tarea = taskToAdd)
            tasks.add(newTask) // Añadir la nueva tarea a la lista
            adaptador.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
            textoEmail.setText("") // Limpiar el campo de texto
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.start()
            }
        }
    }

    /**
     * Inicializa el RecyclerView y carga las tareas desde las preferencias.
     */
    private fun initRecyclerView() {
        tasks = (application as TaskApplication).dbHelper.getTodasTareas()
        rvTareas.layoutManager = LinearLayoutManager(this)
        adaptador = TareaAdaptador(tasks) { eliminarTarea(it) }
        rvTareas.adapter = adaptador

        // Configuración de Swipe to Delete para eliminar tareas al deslizar
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                eliminarTarea(viewHolder.adapterPosition) // Llama al método para eliminar la tarea en la posición desliz
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvTareas) // Asigna el helper al RecyclerView

    }

    /**
     * Elimina una tarea de la lista en la posición especificada y notifica al adaptador.
     *
     * @param position La posición de la tarea a eliminar.
     */
    private fun eliminarTarea(position: Int) {
        val task = tasks[position]

        // Elimina la tarea de la lista
        tasks.removeAt(position)
        adaptador.notifyDataSetChanged()

        // Elimina la tarea de la base de datos usando el id de la tarea específica
        (application as TaskApplication).dbHelper.deleteTarea(task.id)

        // Reproduce sonido si el MediaPlayer para eliminar está inicializado
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.start()
        }
    }


    /**
     * Inicializa las vistas de la interfaz de usuario.
     */
    private fun initView() {
        botonAceptar = findViewById(R.id.botonAceptar)
        textoEmail = findViewById(R.id.textoEmail)
        rvTareas = findViewById(R.id.rvTareas)
    }


    

    /**
     * Libera el MediaPlayer cuando la actividad se destruye para evitar fugas de memoria.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
