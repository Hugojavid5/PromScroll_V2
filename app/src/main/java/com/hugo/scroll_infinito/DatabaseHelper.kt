package com.hugo.scroll_infinito;
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
data class Task(
    val id: Long,
    val tarea: String
)
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLA_TAREA = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TAREA = "tarea"
    }
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLA_TAREA ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TAREA TEXT)"
        db.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_TAREA")
        onCreate(db)
    }
    fun addTarea(tarea: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply { put(COLUMN_TAREA, tarea) }
        return db.insert(TABLA_TAREA, null, values)
    }
    fun getTodasTareas(): MutableList<Task> {
        val tasks = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.query(TABLA_TAREA, arrayOf(COLUMN_ID, COLUMN_TAREA), null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val tarea = getString(getColumnIndexOrThrow(COLUMN_TAREA))
                tasks.add(Task(id, tarea))
            }
            close()
        }
        return tasks
    }   fun deleteTarea(id: Long) {
        val db = writableDatabase
        db.delete(TABLA_TAREA, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

}