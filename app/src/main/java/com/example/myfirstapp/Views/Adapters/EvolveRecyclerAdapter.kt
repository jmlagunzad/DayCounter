package com.example.myfirstapp.Views.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.Model.Log
import com.example.myfirstapp.Model.Transaction
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.Presenter.EducateRecyclerAdapterPresenter
import com.example.myfirstapp.Presenter.EvolveRecyclerAdapterPresenter
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle
import java.lang.Exception
import kotlin.collections.ArrayList


class EvolveRecyclerAdapter(): RecyclerView.Adapter<CustomViewHolder>() {

    var logs : MutableList<Log> = ArrayList()
    private val presenter = EvolveRecyclerAdapterPresenter()

    override fun getItemCount(): Int {
        return logs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder?.view?.textView_mainTitle?.text = logs.get(position).title
        holder?.view?.textView_description?.text = presenter.displayConversion(logs.get(position).value,logs.get(position).unit)
        holder?.view?.textView_deadline?.text = logs.get(position).log_date
    }



}