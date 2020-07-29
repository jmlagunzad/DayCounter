package com.example.myfirstapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.frame_row.view.*

class ExploreRecyclerAdapter: RecyclerView.Adapter<CustomViewHolder>() {

    val items = listOf("Nier GOTY", "P5 Royal", "AC Valhalla")
    val prices = listOf(108, 432, 432)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.frame_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder?.view?.textView_title?.text = items.get(position)
        holder?.view?.textView_Price?.text = "${prices.get(position)} HKD"
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}