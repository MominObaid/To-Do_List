package com.example.to_dolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object DbConstants {
    const val DATABASE_NAME = "ToDoList"
    const val DATABASE_VERSION = 1
    const val TABLE_NAME = "tasks"
    const val COL_ID = "id"
    const val COL_TASK_NAME = "task_name"

}

class TaskDbHelper(context: Context) :
    SQLiteOpenHelper(context, DbConstants.DATABASE_NAME, null, DbConstants.DATABASE_VERSION) {
    override fun onCreate(dp: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE ${DbConstants.TABLE_NAME} (" +
                "${DbConstants.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DbConstants.COL_TASK_NAME} TEXT)"
        dp?.execSQL(createTableQuery)
    }


    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db?.execSQL(
            "DROP TABLE IF EXISTS ${
                DbConstants.TABLE_NAME
            }"
        )
        onCreate(db)

    }

    fun addTask(task: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DbConstants.COL_TASK_NAME, task)
        db.insert(DbConstants.TABLE_NAME, null, contentValues)
        db.close()
    }

    fun getAllTasks(): MutableList<String> {
        val taskList = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DbConstants.TABLE_NAME}", null)

        if (cursor.moveToFirst()) {
            val taskNameIndex = cursor.getColumnIndex(DbConstants.COL_TASK_NAME)
            if (taskNameIndex != -1) {
                do {
                    taskList.add(cursor.getString(taskNameIndex))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return taskList
    }

    fun deleteTask(taskName: String) {
        val db = this.writableDatabase
        db.delete(DbConstants.TABLE_NAME, "${DbConstants.COL_TASK_NAME} = ?", arrayOf(taskName))
        db.close()
    }
}