package com.example.myfirstapp.Presenter

import android.view.View
import android.widget.Spinner
import com.example.myfirstapp.Handlers.TransactionDBHandler
import com.example.myfirstapp.Model.Transaction

class EducatePresenter(view: View) {

    private val transactionHandler = TransactionDBHandler(view.context)

    var query = "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date from transactions ORDER BY id DESC"

    fun getTransactions(): MutableList<Transaction>{
        return transactionHandler.readData(query)
    }

    fun getTransactions(filter: String): MutableList<Transaction>{
        var filterQuery = when(filter){
            "INCOME" -> "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +
                    "where amount > 0.0 " +
                    "ORDER BY id DESC"
            "EXPENSES" -> "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +
                    "where amount < 0.0 " +
                    "ORDER BY id DESC"
            else -> "SELECT id, title, amount, category, strftime('%m/%d',transaction_date) as transaction_date " +
                    "from transactions " +
                    "where category = '$filter' " +
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

    interface OnEditOrDelete {
        fun recompute(computed: Double)
        fun recomputePair(computed: Pair<Double, Double>)
        fun refreshFilterSpinner(categories: List<String>, currentCategory: String)
        fun getCurrentFilter(): String
    }

}