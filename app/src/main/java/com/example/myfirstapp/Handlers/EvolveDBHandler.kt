package com.example.myfirstapp.Handlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.myfirstapp.Model.Log
import com.example.myfirstapp.Model.Transaction
import java.lang.Exception


private val DATABASE_NAME = "Personal"
private val TABLE_NAME = "logs"
private val COL_TITLE = "title"
private val COL_VALUE= "value"
private val COL_UNIT= "unit"
private val COL_DATE= "log_date"
private val COL_ID = "id"

class EvolveDBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 4){

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(log: Log){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_TITLE, log.title)
        cv.put(COL_VALUE, log.value)
        cv.put(COL_UNIT, log.unit)

        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "Adding the log has failed.", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "log added.", Toast.LENGTH_SHORT).show()
        }

    }

    fun readData(query: String) : MutableList<Log>{
        val list: MutableList<Log> = ArrayList()
        val db = this.readableDatabase

        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var log = Log(
                    result.getString(result.getColumnIndex(COL_TITLE)),
                    result.getString(result.getColumnIndex(COL_VALUE)).toDouble(),
                    result.getString(result.getColumnIndex(COL_UNIT))


                )
                log.id = result.getString(0).toInt()
                log.log_date = result.getString(result.getColumnIndex(COL_DATE)).toString()
                list.add(log)
            }while(result.moveToNext())
        }

        return list
    }

    fun updateData(id: Int, log: Log){
        try {
            val db = this.writableDatabase
            var cv = ContentValues()

            cv.put(COL_TITLE, log.title)
            cv.put(COL_VALUE, log.value)
            cv.put(COL_UNIT, log.unit)

            db.update(TABLE_NAME, cv, "$COL_ID = ?", arrayOf(id.toString()))

        }catch(e: Exception){
            Toast.makeText(context, "log update failed. Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    fun deleteData(id: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"$COL_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}