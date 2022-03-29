package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Handlers.EvolveDBHandler
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Model.Log
import com.example.myfirstapp.Model.Transaction
import com.example.myfirstapp.Views.Adapters.EducateRecyclerAdapter
import com.example.myfirstapp.Views.Adapters.EvolveRecyclerAdapter

class EvolveRecyclerAdapterPresenter(view: View) {

    private var logs: MutableList<Log> = ArrayList()
    private val logHandler = EvolveDBHandler(view.context)

    var query = "SELECT id, title, value, unit, strftime('%m/%d',log_date) as log_date from logs"

    fun updateTransaction(title: String, value: String, unit: String, id: Int): MutableList<Log>?{
        try{
            //println("Hello world, presenter here!");
            var log = Log(title, value.toDouble(), unit)
            logHandler.updateData(id, log)
            logs = logHandler.readData(query)
        }
        catch(e: Exception){
            println(e.toString())
            return null
        }
        return logs
    }

    fun deleteRecord(id: Int){
        logHandler.deleteData(id)
    }

    fun displayConversion(value: Double, unit: String, full: Boolean = true): String{
        var result = ""
        result = when(unit){
            "KG" -> {
                var converted = Math.round((Math.round(( Math.round((value * 2.20462) * 1000.0) / 1000.0) * 100.0) / 100.0) * 10.0) / 10.0
                if(full) "$value $unit - $converted LBS" else converted.toString()
            }
            else -> {
                var converted = Math.round((Math.round(( Math.round((value * 0.453592) * 1000.0) / 1000.0) * 100.0) / 100.0) * 10.0) / 10.0
                if(full) "$value $unit - $converted KG" else converted.toString()
            }
        }
        return result
    }

    fun computeDifference(initialWeight:  Double?, currWeight: Double): Double {
        try{
            return Math.round((Math.round(( Math.round((initialWeight!! - currWeight) * 1000.0) / 1000.0) * 100.0) / 100.0) * 10.0) / 10.0
        }catch(e: java.lang.Exception){
            return 0.0
        }
    }

}