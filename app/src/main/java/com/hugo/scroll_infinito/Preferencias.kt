package com.hugo.scroll_infinito

import android.content.Context
import android.content.SharedPreferences
import android.util.Log


/**
 * Trabajara con Shared Preferences
 */

class Preferencias(context: Context) {
    /**
     * Las constatantes de declaran aqui
     */
    companion object{
        const val PREFS_NAME = "misBaseDeDatos"
        const val TAREAS ="valor_tareas"
    }



    /**
     * Creo una base de datos con ese nombre
     */
    val prefs:SharedPreferences = context.getSharedPreferences(PREFS_NAME,0)


    /**
     * Esta funcion trabaja solo con mi constante prefs creada arriba
     */
    fun salvarInformacion(listaDeTareas:List<String>){
        // en prefs edita, pon un string, en el atributo "CAMPO1", pon la frasecilla y después aplica
       // prefs.edit().putString("CAMPO1","el dato de campo 1").apply()
        prefs.edit().putStringSet(TAREAS, listaDeTareas.toSet()).apply()





    }


    /**
     * De la base de datos prefs, lee el atributo TAREAS que sera un Stringset, cambialo a mutable list
     * si no existe devuelve un mutablelistvacio
     */
    fun recuperarTareas():MutableList<String>{


            Log.d("LEYENDO DATOS", "Nada")
        return prefs.getStringSet(TAREAS,  emptySet<String>())?.toMutableList() ?: mutableListOf()
    }

}