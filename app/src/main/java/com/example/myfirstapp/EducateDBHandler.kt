package com.example.myfirstapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.core.database.getStringOrNull
import java.lang.Exception


private val DATABASE_NAME = "Personal"
private val TABLE_NAME = "wishes"
private val COL_NAME = "name"
private val COL_PRICE= "price"
private val COL_CURR= "curr"
private val COL_DEADLINE= "deadline"
private val COL_ID = "id"

class EducateDBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(wish: Wish){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_NAME, wish.name)
        cv.put(COL_PRICE, wish.price)
        cv.put(COL_CURR, wish.curr)
        cv.put(COL_DEADLINE, wish.deadline)

        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "Adding the wish entry has failed.", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Wish entry added.", Toast.LENGTH_SHORT).show()
        }

    }

    fun readData() : MutableList<Wish>{
        val list: MutableList<Wish> = ArrayList()
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * from  $TABLE_NAME", null)
        if(result.moveToFirst()){
            do {
                var wish = Wish(
                    result.getString(result.getColumnIndex(COL_NAME)),
                    result.getString(result.getColumnIndex(COL_PRICE)).toDouble(),
                    result.getString(result.getColumnIndex(COL_CURR)),
                    result.getString(result.getColumnIndex(COL_DEADLINE))
                )
                wish.id = result.getString(0).toInt()
                list.add(wish)
            }while(result.moveToNext())
        }

        return list
    }

    fun updateData(id: Int, wish: Wish){
        try {
            val db = this.writableDatabase
            var cv = ContentValues()

            cv.put(COL_NAME, wish.name)
            cv.put(COL_PRICE, wish.price)
            cv.put(COL_CURR, wish.curr)
            cv.put(COL_DEADLINE, wish.deadline)

            db.update(TABLE_NAME, cv, "$COL_ID = ?", arrayOf(id.toString()))

        }catch(e: Exception){
            Toast.makeText(context, "Wish entry update failed. Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    fun deleteData(id: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"$COL_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}