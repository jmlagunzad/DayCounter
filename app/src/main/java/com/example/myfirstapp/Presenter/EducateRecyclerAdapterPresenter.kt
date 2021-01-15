package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.EducateRecyclerAdapter
import com.example.myfirstapp.Model.Transaction

class EducateRecyclerAdapterPresenter(adapter: EducateRecyclerAdapter) {

    private var transactionHistory: MutableList<Transaction> = ArrayList()
    private var adapter = adapter

//    fun gettransactionHistory(): MutableList<Transaction>{
//        return transactionHistory
//    }

    fun updateTransaction(title: String, amount: String, position: Int): MutableList<Transaction>{
        //println("Hello world, presenter here!");
        var Transaction = Transaction(title, amount.toDouble())
        transactionHistory = adapter.transactions
        transactionHistory.set(position, Transaction)
        return transactionHistory
        //adapter.notifyDataSetChanged()
        //adapter.notifyItemChanged(position)
        //transactionHistory.add(Transaction)
    }

//    fun addNegativeTransaction(title: String, amount: String){
//        //println("Hello world, presenter here!");
//        var Transaction = Transaction(title, amount.toDouble() * -1)
//        transactionHistory.add(Transaction)
//    }
//
    fun computeBalance(transactionHistory: MutableList<Transaction>): Double{
        var total = 0.0
        for(Transaction in transactionHistory) {
            total += Transaction.amount
        }
        return total
    }

}