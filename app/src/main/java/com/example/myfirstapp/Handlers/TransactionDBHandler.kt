package com.example.myfirstapp.Handlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.myfirstapp.Model.Transaction
import java.lang.Exception


private val DATABASE_NAME = "Personal"
private val TABLE_NAME = "transactions"
private val COL_NAME = "title"
private val COL_AMOUNT= "amount"
private val COL_DATE= "transaction_date"
private val COL_ID = "id"
private val COL_CATEGORY = "category"

class TransactionDBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 4){

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
        cv.put(COL_CATEGORY, transaction.category)
        if(transaction.transaction_date != ""){
            cv.put(COL_DATE, transaction.transaction_date)
        }

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
                    result.getString(result.getColumnIndex(COL_AMOUNT)).toDouble(),
                    result.getString(result.getColumnIndex(COL_CATEGORY))
                )
                transaction.id = result.getString(0).toInt()
                transaction.transaction_date = result.getString(result.getColumnIndex(COL_DATE)).toString()
                list.add(transaction)
            }while(result.moveToNext())
        }

        return list
    }

    fun readCategories(): MutableList<String>{
        val list : MutableList<String> = ArrayList()
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT DISTINCT category FROM transactions ORDER by category", null)
        if(result.moveToFirst()){
            do{
                list.add(result.getString(result.getColumnIndex(COL_CATEGORY)))
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
            cv.put(COL_CATEGORY, transaction.category)

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

    fun deleteMultiple(ids: MutableList<Int>){
        val db = this.writableDatabase
//        val list = ids.map { it.toString() }.toTypedArray()
//        list[0] = "(".plus(list[0])
//        list[list.lastIndex] += ")"
        val list = ids.joinToString()
//        list.forEach {
//            Log.e("Handler",it)
//        }
        db.delete(TABLE_NAME,"$COL_ID IN ( $list )",null)
        db.close()
    }

}