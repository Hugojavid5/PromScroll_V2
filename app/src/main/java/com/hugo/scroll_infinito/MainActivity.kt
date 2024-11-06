package com.hugo.scroll_infinito

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {

    private lateinit var botonAceptar: Button
    private lateinit var textoEmail: EditText
    private lateinit var rvTareas: RecyclerView
    private var tareas = mutableListOf<String>()
    private lateinit var adaptador: TareaAdaptador
    private lateinit var mediaPlayer: MediaPlayer
    var tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()

        // Inicializa el MediaPlayer con el sonido para añadir tarea
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_aniadir)
    }

    /**
     * Método para añadir una nueva tarea a la lista y base de datos.
     *
     * @param tarea La tarea que se va a añadir.
     */
    private fun anyadirTarea(tareaTexto: String) {
        // Inserta la tarea en la base de datos y obtiene el ID generado
        val taskId = (application as TaskApplication).dbHelper.addTarea(tareaTexto)

        // Crea la instancia de Task con el ID obtenido y el texto de la tarea
        val tarea = Task(id = taskId, tarea = tareaTexto)

        // Añadir la tarea a la lista de tareas
        tasks.add(tarea)

        // Notificar al adaptador que los datos han cambiado
        adaptador.notifyDataSetChanged()

        // Mostrar mensaje de éxito
        Toast.makeText(this, "Tarea añadida", Toast.LENGTH_SHORT).show()

        // Reproduce el sonido de añadir tarea
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.start()
        }
    }


    /**
     * Inicializa la interfaz de usuario, incluyendo vistas y RecyclerView.
     */
    private fun initUI() {
        initView()
        initRecyclerView()

        // Configura el botón para añadir tareas
        botonAceptar.setOnClickListener {
            val tareaTexto = textoEmail.text.toString().trim()
            if (tareaTexto.isNotEmpty()) {
                anyadirTarea(tareaTexto)
                textoEmail.setText("")  // Limpia el campo de texto
            } else {
                Toast.makeText(this, "Por favor, introduce una tarea", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Inicializa el RecyclerView y carga las tareas desde la base de datos.
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
                eliminarTarea(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvTareas)
    }

    /**
     * Elimina una tarea de la lista y la base de datos, y reproduce el sonido de eliminación.
     *
     * @param position La posición de la tarea a eliminar.
     */
    private fun eliminarTarea(position: Int) {
        val task = tasks[position]

        // Elimina la tarea de la lista y actualiza el adaptador
        tasks.removeAt(position)
        adaptador.notifyItemRemoved(position)

        // Elimina la tarea de la base de datos
        (application as TaskApplication).dbHelper.deleteTarea(task.id)

        // Reproduce el sonido de eliminación
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_eliminar)
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
     * Libera el MediaPlayer al destruir la actividad.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
