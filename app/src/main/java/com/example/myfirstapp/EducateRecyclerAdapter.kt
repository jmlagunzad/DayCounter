package com.example.myfirstapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.Model.Entry
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.Presenter.EducatePresenter.OnEditOrDelete
import com.example.myfirstapp.Presenter.EducateRecyclerAdapterPresenter
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.fragment_educate.*
import kotlinx.android.synthetic.main.fragment_educate.view.*
import kotlinx.android.synthetic.main.frame_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle
import okhttp3.*
import java.util.*
import kotlin.collections.ArrayList


class EducateRecyclerAdapter(listener: EducatePresenter.OnEditOrDelete): RecyclerView.Adapter<CustomViewHolder>() {

    var entries: MutableList<Entry> = ArrayList()
    val adapterPresenter = EducateRecyclerAdapterPresenter(this)
    private val listener = listener

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder?.view?.textView_mainTitle?.text = entries.get(position).title
        holder?.view?.textView_description?.text = entries.get(position).amount.toString()
        if(entries.get(position).amount < 0){
            holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
        }

        holder?.itemView.setOnLongClickListener{

            //CHOOSE TEMPLATE FOR EACH FRAME
            val dialog = AlertDialog.Builder(holder.view.context)
            val layoutInflater = LayoutInflater.from(holder.view.context)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog, null)

            //SET TEXT WITHIN FRAME
            dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit item name"
            dialogView.findViewById<TextView>(R.id.textView_description).text = "Edit item price"
            dialogView.findViewById<EditText>(R.id.editText_title).setText(entries.get(position).title)
            dialogView.findViewById<EditText>(R.id.editText_description).setText(
                entries.get(
                    position
                ).amount.toString()
            )

            //GET VALUES FROM EDIT TEXT FIELDS
            var entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            var entryDescription = dialogView.findViewById<EditText>(R.id.editText_description).text

            //SETUP VALUES FOR DIALOGVIEW
            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton(
                "Save Changes",
                { dialogInterface: DialogInterface, i: Int -> })
            dialog.setNegativeButton(
                "Delete Entry",
                { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()

            //LISTENER FOR EDIT
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{

                if(entryTitle.isNotEmpty()){

                    entries = adapterPresenter.updateEntry(
                        entryTitle.toString(),
                        entryDescription.toString(),
                        position
                    )
                    this.notifyDataSetChanged()
                    this.notifyItemChanged(position)
                    listener.recompute(adapterPresenter.computeBalance(entries))
                    customDialog.dismiss()

                    Toast.makeText(
                        holder.view.context,
                        "Entry ${position + 1} updated.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(holder.view.context, "Enter an entry title!", Toast.LENGTH_LONG).show()
                }
            }

            //LISTENER FOR DELETE
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
                this.entries.removeAt(position)
                this.notifyItemRemoved(position)
                this.notifyItemRangeChanged(position, this.entries.size);
                listener.recompute(adapterPresenter.computeBalance(entries))
                customDialog.dismiss()
                Toast.makeText(
                    holder.view.context,
                    "Entry ${position + 1} deleted.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            true



        }

    }



}