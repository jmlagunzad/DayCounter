package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Model.Transaction

class EducatePresenter(view: View) {

    private var transactionHistory: MutableList<Transaction> = ArrayList()

    fun getTransactions(): MutableList<Transaction>{
        return transactionHistory
    }

    fun addTransaction(title: String, amount: String){
        //println("Hello world, presenter here!");
        var Transaction = Transaction(title, amount.toDouble())
        transactionHistory.add(Transaction)
    }

    fun addNegativeTransaction(title: String, amount: String){
        //println("Hello world, presenter here!");
        var Transaction = Transaction(title, amount.toDouble() * -1)
        transactionHistory.add(Transaction)
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