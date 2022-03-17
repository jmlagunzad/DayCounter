package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Views.Adapters.EducateRecyclerAdapter
import com.example.myfirstapp.Model.Transaction

class EducateRecyclerAdapterPresenter(view: View) {

    private var transactionHistory: MutableList<Transaction> = ArrayList()
    private val transactionHandler = TransactionDBHandler(view.context)

    var query = "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date from transactions ORDER BY id DESC"

    fun updateTransaction(title: String, amount: String, category: String, position: Int): MutableList<Transaction>?{
        try{
            //println("Hello world, presenter here!");
            var transaction = Transaction(title, amount.toDouble(), category)
//        transactionHistory = adapter.transactions
//        transactionHistory.set(position, Transaction)
            transactionHandler.updateData(position, transaction)
            transactionHistory = transactionHandler.readData(query)
        }
        catch(e: Exception){
            println(e.toString())
            return null
        }
        return transactionHistory
    }

    fun getCategories(): MutableList<String>{
        return transactionHandler.readCategories()
    }

    fun deleteTransaction(id: Int){
        transactionHandler.deleteData(id)
    }

    fun deleteTransactions(ids: MutableList<Int>){
        transactionHandler.deleteMultiple(ids)
    }

    fun computeBalance(transactionHistory: MutableList<Transaction>): Double{
        var total = 0.0
        for(transaction in transactionHistory) {
            total += transaction.amount
        }
        return total
    }

    fun tempBalance(transactionHistory: MutableList<Transaction>, isActive: Boolean, position:Int): Pair<Double, Double>{
        var total = 0.0
        var difference = 0.0

        transactionHistory.get(position).active = isActive
//        var activeHistory = transactionHistory.filter { it.active == true }
//        var inactiveHistory = transactionHistory.filter { it.active == false }

        for(transaction in transactionHistory) {
            if(transaction.active){
                total += transaction.amount
            }
            else{
                difference += transaction.amount
            }
        }

        return Pair(total, difference)
    }



}