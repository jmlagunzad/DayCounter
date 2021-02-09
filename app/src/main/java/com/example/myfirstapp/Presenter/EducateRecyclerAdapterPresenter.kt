package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Views.Adapters.EducateRecyclerAdapter
import com.example.myfirstapp.Model.Transaction

class EducateRecyclerAdapterPresenter(view: View, adapter: EducateRecyclerAdapter) {

    private var transactionHistory: MutableList<Transaction> = ArrayList()
    private var adapter = adapter
    private val transactionHandler = TransactionDBHandler(view.context)

    var query = "SELECT id, title, amount, strftime('%m/%d',transaction_date) as transaction_date from transactions ORDER BY id DESC"

    fun updateTransaction(title: String, amount: String, position: Int): MutableList<Transaction>?{
        try{
            //println("Hello world, presenter here!");
            var transaction = Transaction(title, amount.toDouble())
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

    fun deleteTransaction(id: Int){
        transactionHandler.deleteData(id)
    }

    fun computeBalance(transactionHistory: MutableList<Transaction>): Double{
        var total = 0.0
        for(transaction in transactionHistory) {
            total += transaction.amount
        }
        return total
    }

    fun tempBalance(transactionHistory: MutableList<Transaction>, isActive: Boolean, position:Int): Double{
        var total = 0.0

        transactionHistory.get(position).active = isActive

        for(transaction in transactionHistory) {
            if(transaction.active){
                total += transaction.amount
            }
        }

        return total
    }



}