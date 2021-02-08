package com.example.myfirstapp.Views.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.Model.Transaction
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.Presenter.EducateRecyclerAdapterPresenter
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle
import java.lang.Exception
import kotlin.collections.ArrayList


class EducateRecyclerAdapter(view: View,listener: EducatePresenter.OnEditOrDelete): RecyclerView.Adapter<CustomViewHolder>() {

    private val listener = listener

    var transactions: MutableList<Transaction> = ArrayList()
    val adapterPresenter = EducateRecyclerAdapterPresenter(view, this)


    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row_v2, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder?.view?.textView_mainTitle?.text = transactions.get(position).title
        holder?.view?.textView_description?.text = transactions.get(position).amount.toString()
        if(transactions.get(position).amount < 0.0){
            holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
        }
        else{
            holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
        }
        holder?.view?.textView_deadline?.text = transactions.get(position).transaction_date

        holder?.itemView.setOnLongClickListener{

            //CHOOSE TEMPLATE FOR EACH FRAME
            val dialog = AlertDialog.Builder(holder.view.context)
            val layoutInflater = LayoutInflater.from(holder.view.context)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog, null)

            //SET TEXT WITHIN FRAME
            dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit item name"
            dialogView.findViewById<TextView>(R.id.textView_description).text = "Edit item price"
            dialogView.findViewById<EditText>(R.id.editText_title).setText(transactions.get(position).title)
            dialogView.findViewById<EditText>(R.id.editText_description).setText(
                transactions.get(
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

                try {

                    transactions = adapterPresenter.updateTransaction(
                        entryTitle.toString(),
                        entryDescription.toString(),
                        transactions.get(position).id
                    )!!
                    this.notifyDataSetChanged()
                    this.notifyItemChanged(position)
                    listener.recompute(adapterPresenter.computeBalance(transactions))
                    customDialog.dismiss()

                    Toast.makeText(
                        holder.view.context,
                        "Entry ${position + 1} updated.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                catch(e: Exception){
                    println(e.toString())
                    Toast.makeText(holder.view.context, "Enter a proper amount", Toast.LENGTH_LONG).show()
                }
            }

            //LISTENER FOR DELETE
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
                adapterPresenter.deleteTransaction(transactions.get(position).id)
                this.transactions.removeAt(position)
                this.notifyItemRemoved(position)
                this.notifyItemRangeChanged(position, this.transactions.size);
                listener.recompute(adapterPresenter.computeBalance(transactions))
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