package com.hugo.scroll_infinito

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
/**
 * Representa una tarea con un ID único y una descripción.
 *
 * @property id El ID único de la tarea en la base de datos.
 * @property description La descripción de la tarea.
 */
data class Task(
    val id: Long,            // ID único para cada tarea en la base de datos
    val description: String   // Descripción de la tarea
)
/**
 * Ayudante de base de datos para gestionar las operaciones relacionadas con las tareas.
 *
 * @param context El contexto de la aplicación, usado para crear la base de datos.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DESCRIPTION = "description"
    }
    /**
     * Crea la tabla de tareas en la base de datos.
     *
     * @param db La base de datos en la que se crea la tabla.
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_TASKS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_DESCRIPTION TEXT)"
        db.execSQL(createTable)
    }
    /**
     * Actualiza la base de datos al cambiar la versión.
     *
     * @param db La base de datos que se está actualizando.
     * @param oldVersion La versión anterior de la base de datos.
     * @param newVersion La nueva versión de la base de datos.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }
    /**
     * Añade una nueva tarea a la base de datos.
     *
     * @param description La descripción de la tarea a añadir.
     * @return El ID de la tarea recién añadida.
     */
    fun addTask(description: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply { put(COLUMN_DESCRIPTION, description) }
        return db.insert(TABLE_TASKS, null, values)
    }
    /**
     * Recupera todas las tareas de la base de datos.
     *
     * @return Una lista mutable de tareas.
     */
    fun getTasks(): MutableList<Task> {
        val tasks = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.query(TABLE_TASKS, arrayOf(COLUMN_ID, COLUMN_DESCRIPTION), null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                tasks.add(Task(id, description))
            }
            close()
        }
        return tasks
    }
    /**
     * Elimina una tarea de la base de datos por su ID.
     *
     * @param id El ID de la tarea a eliminar.
     */
    fun deleteTask(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_TASKS, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}