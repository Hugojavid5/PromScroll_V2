package com.hugo.scroll_infinito;

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.hugo.scroll_infinito.R

/**
 * Creo una clase que aprovecha un codigo externo, el del adapter del recycleview
 * a ese recycle view le paso el modelo de ViewHolder
 * como vamos a llamar a este adaptador desde un SetOnClickListener y queremos hacer referencia aquien le ha llamado, ponemos el onItemDone
 */

class TareaAdaptador( private val tareas: MutableList<Task>, private val onItemDone: (Int) -> Unit):RecyclerView.Adapter<TareasViewHolder>() {


 /**
  * Relaciona el vision con el ViewHolder usando el contexto del parent, solo al crearlo
  */
 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
  //usando el XML del resources, lo asigno
   val layouInflater:LayoutInflater = LayoutInflater.from(parent.context)
  return TareasViewHolder(layouInflater.inflate(R.layout.item_tarea, parent, false))
 }

 /**
  * Cuantos elementos a mostrar
  */
 override fun getItemCount() = tareas.size

 /**
  * Dibuja cada elemento segun necesita según la posición y los datos del listado de tareas
  * Le pasamos el elemento onItemDone para que sepa quien le ha pulsado
  */
 override fun onBindViewHolder(holder: TareasViewHolder, position: Int) {
  holder.render(tareas[position].description) { onItemDone(position) }
 }




}
