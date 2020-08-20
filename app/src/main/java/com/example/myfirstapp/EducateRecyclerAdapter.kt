package com.example.myfirstapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle
import okhttp3.*
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

class EducateRecyclerAdapter(): RecyclerView.Adapter<CustomViewHolder>() {

    //var entries: MutableList<Entry> = ArrayList()
    var wishes: MutableList<EducateFragment.Wish> = ArrayList()
    //var attempts: MutableList<Attempt> = ArrayList()
    var exchangeRate: Double = 0.0




    override fun getItemCount(): Int {
        //return entries.size
        //return contents.data.count()
        //return entries.size
        return wishes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //var contentList = contents.data.get(position)
        //var contentList = contents.data
        //holder?.view?.textView_mainTitle.text = exchangeRate.HKD_PHP.toString()
        //holder?.view?.textView_description.text = "${contentList.first_name} ${contentList.last_name}"
        var priceInPeso = BigDecimal(wishes.get(position).price * exchangeRate).setScale(2, RoundingMode.HALF_EVEN).toDouble()
        when(priceInPeso){
            in 1.00..499.99 -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
            in 500.00..999.99 -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFDFBA"))
            else -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
        }

        holder?.view?.textView_mainTitle?.text = wishes.get(position).title
        holder?.view?.textView_description?.text = "$priceInPeso PHP - ${wishes.get(position).price} HKD"

        val db = EducateDBHandler(holder.view.context)

        holder?.itemView.setOnLongClickListener{
            //Toast.makeText(holder.view.context, "id: ${entries.get(position).id} posInList: $position", Toast.LENGTH_LONG).show()
            //Toast.makeText(holder.view.context, holder?.view?.textView_mainTitle?.text, Toast.LENGTH_LONG).show()
            val dialog = AlertDialog.Builder(holder.view.context)
            val layoutInflater = LayoutInflater.from(holder.view.context)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog,null)

            dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit entry"
            dialogView.findViewById<TextView>(R.id.textView_description).text = "Edit price"
            dialogView.findViewById<EditText>(R.id.editText_title).setText(wishes.get(position).title)
            dialogView.findViewById<EditText>(R.id.editText_description).setText(wishes.get(position).price.toString())

            var entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            var entryDescription = dialogView.findViewById<EditText>(R.id.editText_description).text

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Save Changes", { dialogInterface: DialogInterface, i: Int -> })
            dialog.setNegativeButton("Delete Entry", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{


                if(entryTitle.isNotEmpty()){
                    //println(entryDescription.toString())
                    if(entryDescription.toString().matches("(?<=^| )\\d+(\\.\\d+)?(?=\$| )".toRegex())) {
//                        val newEntry = Entry(entryTitle.toString(), entryDescription.toString())
//                        db.updateData(entries.get(position).id, newEntry)
//                        this.entries = db.readData()


                        this.notifyItemChanged(position)
                        customDialog.dismiss()

                        Toast.makeText(
                            holder.view.context,
                            "Entry ${position + 1} updated.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        Toast.makeText(holder.view.context, "Enter only numbers.", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(holder.view.context, "Enter a title!", Toast.LENGTH_LONG).show()
                }
            }

            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
//                db.deleteData(entries.get(position).id)
//                this.entries.removeAt(position)
                this.notifyItemRemoved(position)
//                this.notifyItemRangeChanged(position, this.entries.size);
                customDialog.dismiss()
                Toast.makeText(holder.view.context, "Entry ${position + 1} deleted.", Toast.LENGTH_SHORT).show()
            }


            true
        }
    }

}