package com.example.myfirstapp.Handlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.myfirstapp.Model.Transaction
import java.lang.Exception


private val DATABASE_NAME = "Personal"
private val TABLE_NAME = "transactions"
private val COL_NAME = "title"
private val COL_AMOUNT= "amount"
private val COL_DATE= "transaction_date"
private val COL_ID = "id"

class TransactionDBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 3){

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(transaction: Transaction){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_NAME, transaction.title)
        cv.put(COL_AMOUNT, transaction.amount)
        //cv.put(COL_DATE, )

        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "Adding the transaction entry has failed.", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "transaction entry added.", Toast.LENGTH_SHORT).show()
        }

    }

    fun readData(query: String) : MutableList<Transaction>{
        val list: MutableList<Transaction> = ArrayList()
        val db = this.readableDatabase

        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var transaction = Transaction(
                    result.getString(result.getColumnIndex(COL_NAME)),
                    result.getString(result.getColumnIndex(COL_AMOUNT)).toDouble()

                )
                transaction.id = result.getString(0).toInt()
                transaction.transaction_date = result.getString(result.getColumnIndex(COL_DATE)).toString()
                list.add(transaction)
            }while(result.moveToNext())
        }

        return list
    }

    fun updateData(id: Int, transaction: Transaction){
        try {
            val db = this.writableDatabase
            var cv = ContentValues()

            cv.put(COL_NAME, transaction.title)
            cv.put(COL_AMOUNT, transaction.amount)

            db.update(TABLE_NAME, cv, "$COL_ID = ?", arrayOf(id.toString()))

        }catch(e: Exception){
            Toast.makeText(context, "transaction entry update failed. Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    fun deleteData(id: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"$COL_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}