package com.example.myfirstapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.core.database.getStringOrNull
import java.lang.Exception

val DATABASE_NAME = "Personal"
val TABLE_NAME = "Entries"
val COL_TITLE = "title"
val COL_DESCRIPTION = "description"
val COL_ID = "id"

class DatabaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_TITLE VARCHAR(256)," +
                "$COL_DESCRIPTION VARCHAR(256) NULL)"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(entry: Entry){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_TITLE, entry.title)
        cv.put(COL_DESCRIPTION, entry.description)

        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "Adding the entry has failed.", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Entry added.", Toast.LENGTH_SHORT).show()
        }

    }

    fun readData() : MutableList<Entry>{
        val list: MutableList<Entry> = ArrayList()
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * from  $TABLE_NAME", null)
        if(result.moveToFirst()){
            do {
                var entry = Entry(result.getString(result.getColumnIndex(COL_TITLE)),
                                                    result.getStringOrNull(result.getColumnIndex(COL_DESCRIPTION)))
                entry.id = result.getString(0).toInt()
                list.add(entry)
            }while(result.moveToNext())
        }

        return list
    }

    fun updateData(id: Int, entry: Entry){
        try {
            val db = this.writableDatabase
            var cv = ContentValues()

            cv.put(COL_TITLE, entry.title)
            cv.put(COL_DESCRIPTION, entry.description)

            db.update(TABLE_NAME, cv, "$COL_ID = ?", arrayOf(id.toString()))

        }catch(e: Exception){
            Toast.makeText(context, "Entry update failed. Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    fun deleteData(id: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"$COL_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}