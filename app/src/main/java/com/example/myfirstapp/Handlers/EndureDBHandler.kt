package com.example.myfirstapp.Handlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.myfirstapp.Model.Attempt
import java.lang.Exception

private val DATABASE_NAME = "Personal"
private val TABLE_NAME = "Attempts"
private val COL_START = "start_date"
private val COL_END = "end_date"
private val COL_DAYS = "days"
private val COL_ID = "id"

class EndureDBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 4){

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")

    }

    fun insertData(attempt: Attempt){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_START, attempt.start)
        cv.put(COL_END, attempt.end)
        cv.put(COL_DAYS, attempt.days)

        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "Adding the attempt has failed.", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Attempt added.", Toast.LENGTH_SHORT).show()
        }

    }

    fun checkExist(): Boolean{
        val db = this.readableDatabase
        var doesExist = false

        val result = db.rawQuery("SELECT EXISTS(SELECT 1 FROM $TABLE_NAME WHERE days = 0 ORDER BY id DESC LIMIT 1)", null)

        result.moveToFirst()
        if(result.getInt(0) == 1) {
            doesExist = true
        }

        return doesExist
    }

    fun readData() : Attempt {
       // val list: MutableList<Entry> = ArrayList()
        val db = this.readableDatabase
        var ret = Attempt()

        val result = db.rawQuery("SELECT * from  $TABLE_NAME WHERE days = 0 order by id desc limit 1", null)
        if(result.moveToFirst()){
            do {
                var attempt = Attempt(result.getString(result.getColumnIndex(COL_START)))
                attempt.end = result.getString(result.getColumnIndex(COL_END))
                attempt.days = result.getString(result.getColumnIndex(COL_DAYS)).toInt()
                attempt.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                ret = attempt

            }while(result.moveToNext())
        }

        return ret
    }

    fun updateData(id: Int, attempt: Attempt){
        try {
            val db = this.writableDatabase
            var cv = ContentValues()

            //cv.put(COL_START, attempt.start)
            cv.put(COL_END, attempt.end)
            cv.put(COL_DAYS, attempt.days)

            db.update(TABLE_NAME, cv, "$COL_ID = ?", arrayOf(attempt.id.toString()))

            Toast.makeText(context, "Handler: Attempt updated successfully!", Toast.LENGTH_LONG).show()

        }catch(e: Exception){
            Toast.makeText(context, "Handler: Attempt update failed. Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    fun readHistory(): MutableList<Attempt>{

        val db = this.readableDatabase
        var attempts: MutableList<Attempt> = ArrayList()

        val result = db.rawQuery("SELECT * from  $TABLE_NAME WHERE end_date != \"\" order by id desc limit 5", null)
        if(result.moveToFirst()){
            do {
                var attempt = Attempt(result.getString(result.getColumnIndex(COL_START)))
                attempt.end = result.getString(result.getColumnIndex(COL_END))
                attempt.days = result.getString(result.getColumnIndex(COL_DAYS)).toInt()
                attempt.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                attempts.add(attempt)

            }while(result.moveToNext())
        }

        return attempts
    }

}