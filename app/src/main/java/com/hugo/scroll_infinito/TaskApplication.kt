package com.hugo.scroll_infinito

import android.app.Application


/**
 * Punto de entrada de la aplicación
 */

class TaskApplication:Application() {
    /**
     * Para que sea accesible desde toda la aplicacíon, lo pongo aquí, uso en lateInit porque no
     * tengo baseContext para inizalizarlo
     */
    lateinit var dbHelper: DatabaseHelper

    override fun onCreate() {
        super.onCreate()
        dbHelper = DatabaseHelper(this)
    }
}