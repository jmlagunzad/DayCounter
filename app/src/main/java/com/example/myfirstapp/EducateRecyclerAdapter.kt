package com.example.myfirstapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.Model.Entry
import com.example.myfirstapp.Model.Wish
import com.example.myfirstapp.Views.EducateFragment
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.fragment_educate.*
import kotlinx.android.synthetic.main.fragment_educate.view.*
import kotlinx.android.synthetic.main.frame_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

class EducateRecyclerAdapter(): RecyclerView.Adapter<CustomViewHolder>() {

    var entries: MutableList<Entry> = ArrayList()

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

    }

}