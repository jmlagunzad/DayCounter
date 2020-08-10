package com.example.myfirstapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.core.database.getStringOrNull
import java.lang.Exception


private val DATABASE_NAME = "Personal"
private val TABLE_NAME = "Wishes"
private val COL_TITLE = "title"
private val COL_PRICE= "price"
private val COL_ID = "id"

class EducateDBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(entry: Entry){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_TITLE, entry.title)
        cv.put(COL_PRICE, entry.description!!.toDouble())

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
                    result.getStringOrNull(result.getColumnIndex(COL_PRICE)))
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
            cv.put(COL_PRICE, entry.description!!.toDouble())

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