package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Handlers.EvolveDBHandler
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Model.Transaction
import com.example.myfirstapp.Views.Adapters.EducateRecyclerAdapter
import com.example.myfirstapp.Views.Adapters.EvolveRecyclerAdapter

class EvolveRecyclerAdapterPresenter(view: View) {

//    private var transactionHistory: MutableList<Transaction> = ArrayList()
//    private var adapter = adapter
    private val logHandler = EvolveDBHandler(view.context)
//
//    var query = "SELECT id, title, amount, strftime('%m/%d',transaction_date) as transaction_date from transactions ORDER BY id DESC"
//
//    fun updateTransaction(title: String, amount: String, position: Int): MutableList<Transaction>?{
//        try{
//            //println("Hello world, presenter here!");
//            var transaction = Transaction(title, amount.toDouble())
////        transactionHistory = adapter.transactions
////        transactionHistory.set(position, Transaction)
//            transactionHandler.updateData(position, transaction)
//            transactionHistory = transactionHandler.readData(query)
//        }
//        catch(e: Exception){
//            println(e.toString())
//            return null
//        }
//        return transactionHistory
//    }
//
    fun deleteRecord(id: Int){
        logHandler.deleteData(id)
    }
//

    fun displayConversion(value: Double, unit: String): String{
        var result = ""
        result = when(unit){
            "KG" -> {
                var converted = Math.round((Math.round(( Math.round((value * 2.20462) * 1000.0) / 1000.0) * 100.0) / 100.0) * 10.0) / 10.0
                "$value $unit - $converted LBS"
            }
            else -> {
                var converted = Math.round((Math.round(( Math.round((value * 0.453592) * 1000.0) / 1000.0) * 100.0) / 100.0) * 10.0) / 10.0
                "$value $unit - $converted KG"
            }
        }
        return result
    }

}