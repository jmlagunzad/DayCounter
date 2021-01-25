package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Model.Transaction

class EducatePresenter(view: View) {

    private var transactionHistory: MutableList<Transaction> = ArrayList()
    private val transactionHandler = TransactionDBHandler(view.context)

    var query = "SELECT id, title, amount, strftime('%m/%d',transaction_date) as transaction_date from transactions"

    fun getTransactions(): MutableList<Transaction>{
        transactionHistory = transactionHandler.readData(query)
        return transactionHistory
    }

    fun addTransaction(title: String, amount: String){
        //println("Hello world, presenter here!");
        var transaction = Transaction(title, amount.toDouble())
        //transactionHistory.add(Transaction)
        transactionHandler.insertData(transaction)
    }

    fun addNegativeTransaction(title: String, amount: String){
        //println("Hello world, presenter here!");
        var transaction = Transaction(title, amount.toDouble() * -1)
        //transactionHistory.add(Transaction)
        transactionHandler.insertData(transaction)
    }

    fun computeBalance(transactionHistory: MutableList<Transaction>): Double{
        var total = 0.0
        for(Transaction in transactionHistory) {
            total += Transaction.amount
        }
        return total
    }

    interface OnEditOrDelete {
        fun recompute(computed: Double)
    }

}