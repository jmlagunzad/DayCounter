package com.example.myfirstapp.Presenter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Model.Transaction
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

class EducatePresenter(view: View) {

    private val transactionHandler = TransactionDBHandler(view.context)

    fun getTransactions(): MutableList<Transaction>{
        var query = "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                "from transactions " +
                "ORDER BY id DESC"
        return transactionHandler.readData(query)
    }

    fun getTransactionsFullDate(): MutableList<Transaction>{
        var query = "SELECT id, title, amount, category, transaction_date " +
                "from transactions " +
                "ORDER BY id DESC"
        return transactionHandler.readData(query)
    }

    fun getTransactions(filter: String, type: String): MutableList<Transaction>{
        var typeFilter = when(type){
            "INCOME" -> " AND amount > 0.0 "
            "EXPENSES" -> " AND amount < 0.0 "
            else -> " "
        }

        var filterQuery = when(filter){
//            "INCOME" -> "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
//                    "from transactions " +
//                    "where amount > 0.0 " +
//                    "ORDER BY id DESC"
//            "EXPENSES" -> "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
//                    "from transactions " +
//                    "where amount < 0.0 " +
//                    "ORDER BY id DESC"
            "ALL" -> "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions where 1 " +
                    typeFilter +
                    "ORDER BY id DESC"
            "THIS CUTOFF" ->    "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +
                    "where CASE " +

                    "WHEN CAST(strftime('%d',datetime('now')) as integer) > 10 AND CAST(strftime('%d',datetime('now')) as integer) < 26 " +
                    "THEN transaction_date between strftime('%Y-%m-11',datetime('now')) " +
                    "AND strftime('%Y-%m-26',datetime('now','localtime')) " +

                    "WHEN CAST(strftime('%d',datetime('now')) as integer) < 11 " +
                    "THEN transaction_date between strftime('%Y-%m-26',datetime('now','start of month','-1 months')) " +
                    "AND strftime('%Y-%m-11',datetime('now')) " +

                    "ELSE transaction_date between strftime('%Y-%m-26',datetime('now')) " +
                    "AND strftime('%Y-%m-11',datetime('now','start of month','+1 months')) " +

                    "END " +
                    typeFilter +
                    "ORDER BY id DESC"
            "LAST CUTOFF" ->    "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +
                    "where CASE " +

                    "WHEN CAST(strftime('%d',datetime('now')) as integer) > 10 AND CAST(strftime('%d',datetime('now')) as integer) < 26 " +
                    "THEN transaction_date between strftime('%Y-%m-26',datetime('now','start of month','-1 months')) " +
                    "AND strftime('%Y-%m-11',datetime('now')) " +

                    "WHEN CAST(strftime('%d',datetime('now')) as integer) < 11 " +
                    "THEN transaction_date between strftime('%Y-%m-11',datetime('now','start of month','-1 months')) " +
                    "AND strftime('%Y-%m-26',datetime('now','start of month','-1 months')) " +

                    "ELSE transaction_date between strftime('%Y-%m-11',datetime('now')) " +
                    "AND strftime('%Y-%m-26',datetime('now')) " +

                    "END " +
                    typeFilter +
                    "ORDER BY id DESC"
            "THIS MONTH" ->    "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +

                    "WHERE transaction_date between strftime('%Y-%m-%d',datetime('now', 'start of month')) " +
                    "AND strftime('%Y-%m-%d',datetime('now','start of month','+1 month','-1 day')) " +

                    typeFilter +
                    "ORDER BY id DESC"

            "LAST MONTH" ->    "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +

                    "WHERE transaction_date between strftime('%Y-%m-%d',datetime('now','start of month','-1 months')) " +
                    "AND strftime('%Y-%m-%d',datetime('now','start of month','-1 day')) " +

                    typeFilter +
                    "ORDER BY id DESC"
            else -> "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +
                    "where category = '$filter' " +
                    typeFilter +
                    "ORDER BY id DESC"
        }
        return transactionHandler.readData(filterQuery)
    }

    fun getCategories(): MutableList<String> {
        return transactionHandler.readCategories()
    }

    fun addTransaction(title: String, amount: String, category: String): Boolean{
        try{
            var transaction = Transaction(title, amount.toDouble(), category)
            transactionHandler.insertData(transaction)
        }
        catch(e: Exception){
            println(e.toString())
            return false
        }
        return true
    }

    fun addNegativeTransaction(title: String, amount: String, category: String): Boolean{
        try{
            var transaction = Transaction(title, amount.toDouble() * -1, category)
            transactionHandler.insertData(transaction)
        }
        catch(e: Exception){
            println(e.toString())
            return false
        }
        return true
    }

    fun computeBalance(transactionHistory: MutableList<Transaction>): Double{
        var total = 0.0
        for(Transaction in transactionHistory) {
            total += Transaction.amount
        }
        return total
    }

//    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(Build.VERSION_CODES.R)
    fun exportData(): Boolean{
        val exportDir = File(Environment.getExternalStorageDirectory(),"MyMorgana")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

//        val fos : FileOutputStream? = null
//        val fos : FileOutputStream = context!!.openFileOutput("csvname.csv", MODE_PRIVATE)
//        fos = context!!.openFileOutput("csvname.csv", MODE_PRIVATE)

        val file = File(exportDir, "EducateFile.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val curCSV = getTransactionsFullDate()

            csvWrite.writeNext(arrayOf("title","amount","category","transaction_date"))
            curCSV.forEach{
                csvWrite.writeNext(arrayOf(
                    it.title,
                    it.amount.toString(),
                    it.category,
                    it.transaction_date
                ))
            }
            csvWrite.close()
            return true
        } catch (sqlEx: java.lang.Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
            return false
        }


    }

    interface OnEditOrDelete {
        fun recompute(computed: Double)
        fun recomputePair(computed: Pair<Double, Double>)
        fun refreshSpinner(spinner: Spinner, categories: List<String>, currentCategory: String)
        fun refreshSpinner(categories: List<String>, currentCategory: String)
        fun getCurrentFilter(): String
//        fun exportData()
    }

}