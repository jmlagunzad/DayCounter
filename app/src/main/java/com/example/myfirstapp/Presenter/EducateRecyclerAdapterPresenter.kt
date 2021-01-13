package com.example.myfirstapp.Presenter

import android.view.View
import com.example.myfirstapp.EducateRecyclerAdapter
import com.example.myfirstapp.Model.Entry

class EducateRecyclerAdapterPresenter(adapter: EducateRecyclerAdapter) {

    private var entries: MutableList<Entry> = ArrayList()
    private var adapter = adapter

//    fun getEntries(): MutableList<Entry>{
//        return entries
//    }

    fun updateEntry(title: String, amount: String, position: Int): MutableList<Entry>{
        //println("Hello world, presenter here!");
        var entry = Entry(title, amount.toDouble())
        entries = adapter.entries
        entries.set(position, entry)
        return entries
        //adapter.notifyDataSetChanged()
        //adapter.notifyItemChanged(position)
        //entries.add(entry)
    }

//    fun addNegativeEntry(title: String, amount: String){
//        //println("Hello world, presenter here!");
//        var entry = Entry(title, amount.toDouble() * -1)
//        entries.add(entry)
//    }
//
    fun computeBalance(entries: MutableList<Entry>): Double{
        var total = 0.0
        for(entry in entries) {
            total += entry.amount
        }
        return total
    }

}