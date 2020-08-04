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

        /*holder?.itemView.setOnLongClickListener{
            Toast.makeText(holder.view.context, "id: ${entries.get(position).id} posInList: $position", Toast.LENGTH_LONG).show()
            //Toast.makeText(holder.view.context, holder?.view?.textView_mainTitle?.text, Toast.LENGTH_LONG).show()
            val dialog = AlertDialog.Builder(holder.view.context)
            val layoutInflater = LayoutInflater.from(holder.view.context)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog,null)

            dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit entry"
            dialogView.findViewById<EditText>(R.id.editText_title).setText(entries.get(position).title)
            dialogView.findViewById<EditText>(R.id.editText_description).setText(entries.get(position).description)

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
                    val newEntry = Entry(entryTitle.toString(), entryDescription.toString())
                    db.updateData(entries.get(position).id, newEntry)
                    this.entries = db.readData()
                    this.notifyItemChanged(position)
                    customDialog.dismiss()

                    Toast.makeText(holder.view.context, "Entry ${position+1} updated.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(holder.view.context, "Enter a title!", Toast.LENGTH_LONG).show()
                }
            }

            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
                db.deleteData(entries.get(position).id)
                this.entries.removeAt(position)
                this.notifyItemRemoved(position)
                this.notifyItemRangeChanged(position, this.entries.size);
                customDialog.dismiss()
                Toast.makeText(holder.view.context, "Entry ${position + 1} deleted.", Toast.LENGTH_SHORT).show()
            }


            true
        }
    }*/
    }

}

/*class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}*/