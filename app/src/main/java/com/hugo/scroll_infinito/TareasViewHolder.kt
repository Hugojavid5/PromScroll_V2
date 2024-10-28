package com.hugo.scroll_infinito
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hugo.scroll_infinito.R

/**
 * Voy a devolver un viewholder al propio view que me llamo
 *
 * */


class TareasViewHolder(view:View):RecyclerView.ViewHolder(view) {

    /**
     * Creo la referencia y la inicializo directamente contra el xml
     */
    private val tvTarea:TextView = view.findViewById(R.id.tvTarea)

    /**
     * Crea la referencia y la inizcializa contra el xml
     */
    private val ivTareaHecha:ImageView = view.findViewById(R.id.ivTareaHecha)

    /**
     * EL adapater al conectarse tiene el listado completo de tareas,
     * de ahí coge el string
     * @param tarea la tarea a dibujar
     * @onItemDone el elmento que va a dibujar
     */
    fun render(tarea:String, onItemDone:(Int) -> Unit){
        tvTarea.text = tarea
        // añadimos un onclicklistener a la imagen, llamara a una funcion en el TareaAdapter
        ivTareaHecha.setOnClickListener { onItemDone(adapterPosition)  }

    }

}