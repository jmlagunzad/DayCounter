package com.example.myfirstapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.frame_row.view.*
import kotlin.coroutines.coroutineContext


class EndureRecyclerAdapter: RecyclerView.Adapter<CustomViewHolder>() {

    //    val entry1 = Entry("Nier GOTY", "235")
    //   val entry2 = Entry("Bloodborne", "88")
    var attempts: MutableList<Attempt> = ArrayList()

    override fun getItemCount(): Int {
        return attempts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.frame_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder?.view?.textView_mainTitle?.text = "${attempts.get(position).start} to ${attempts.get(position).end}"
        holder?.view?.textView_description?.text = "${attempts.get(position).days} days"
        val db = EndureDBHandler(holder.view.context)


    }

}

/*class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}*/