package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Handlers.EvolveDBHandler
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Model.Log
import com.example.myfirstapp.Model.Transaction

class EvolvePresenter(view: View) {

    private var logs: MutableList<Log> = ArrayList()
    private val logHandler = EvolveDBHandler(view.context)

    var query = "SELECT id, title, value, unit, strftime('%m/%d',log_date) as log_date from logs"

    fun getLogs(): MutableList<Log>{
        logs = logHandler.readData(query)
        return logs
    }

    fun addLog(title: String, value: String, unit: String): Boolean{
        try{
            var log = Log(title, value.toDouble(), unit)
            logHandler.insertData(log)
        }
        catch(e: Exception){
            println(e.toString())
            return false
        }
        return true
    }

//    fun computeBalance(transactionHistory: MutableList<Transaction>): Double{
//        var total = 0.0
//        for(Transaction in transactionHistory) {
//            total += Transaction.amount
//        }
//        return total
//    }

}