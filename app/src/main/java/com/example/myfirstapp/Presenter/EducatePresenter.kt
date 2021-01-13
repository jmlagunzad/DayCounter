package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.Model.Entry

class EducatePresenter(view: View) {

    private var entries: MutableList<Entry> = ArrayList()

    fun getEntries(): MutableList<Entry>{
        return entries
    }

    fun addEntry(title: String, amount: String){
        //println("Hello world, presenter here!");
        var entry = Entry(title, amount.toDouble())
        entries.add(entry)
    }

    fun addNegativeEntry(title: String, amount: String){
        //println("Hello world, presenter here!");
        var entry = Entry(title, amount.toDouble() * -1)
        entries.add(entry)
    }

    fun computeBalance(entries: MutableList<Entry>): Double{
        var total = 0.0
        for(entry in entries) {
            total += entry.amount
        }
        return total
    }

}