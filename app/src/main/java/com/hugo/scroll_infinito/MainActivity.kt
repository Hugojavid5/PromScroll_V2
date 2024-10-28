package com.hugo.scroll_infinito

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hugo.scroll_infinito.TaskApplication.Companion.prefs

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
    }

    /**
     * Inicializa la interfaz de usuario, incluyendo vistas, oyentes y el RecyclerView.
     */
    private fun initUI() {
        initView()
        initListeners()
        initRecyclerView()
    }

    /**
     * Añade una nueva tarea a la lista y notifica al adaptador sobre el cambio.
     *
     * @param newTask La tarea que se va a añadir.
     */
    private fun addTask(newTask: String) {
        tareas.add(newTask)
        adaptador.notifyDataSetChanged()
        prefs.salvarInformacion(tareas)

        // Reproduce sonido si el MediaPlayer está inicializado
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.start()
        }
    }

    /**
     * Inicializa el RecyclerView y carga las tareas desde las preferencias.
     */
    private fun initRecyclerView() {
        tareas = prefs.recuperarTareas()
        rvTareas.layoutManager = LinearLayoutManager(this)
        adaptador = TareaAdaptador(tareas) { eliminarTarea(it) }
        rvTareas.adapter = adaptador
    }

    /**
     * Elimina una tarea de la lista en la posición especificada y notifica al adaptador.
     *
     * @param position La posición de la tarea a eliminar.
     */
    private fun eliminarTarea(position: Int) {
        tareas.removeAt(position)
        adaptador.notifyDataSetChanged()
        prefs.salvarInformacion(tareas)
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
     * Inicializa los oyentes para los eventos de la interfaz de usuario.
     */
    private fun initListeners() {
        botonAceptar.setOnClickListener { anyadirTarea() }
    }

    /**
     * Añade una tarea a la lista tomando el texto del EditText.
     * Limpia el EditText después de añadir la tarea.
     */
    private fun anyadirTarea() {
        val tareaAAnyadir = textoEmail.text.toString().trim()
        if (tareaAAnyadir.isNotEmpty()) {
            addTask(tareaAAnyadir)
            textoEmail.setText("")
        }
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
