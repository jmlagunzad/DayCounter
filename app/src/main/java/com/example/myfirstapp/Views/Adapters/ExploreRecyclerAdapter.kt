package com.example.myfirstapp.Views.Adapters

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.Handlers.EducateDBHandler
import com.example.myfirstapp.Model.Wish
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class ExploreRecyclerAdapter(): RecyclerView.Adapter<CustomViewHolder>() {

    var wishes: MutableList<Wish> = ArrayList()
    var query = ""
    var hkdRate = 6.0
    var usdRate = 50.0


    override fun getItemCount(): Int {
        return wishes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row, parent, false)
        return CustomViewHolder(cellForRow)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var priceInPeso = 0.0
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var deadline = LocalDate.parse(wishes.get(position).deadline, formatter)

        when(wishes.get(position).curr){
            "HKD" -> priceInPeso = BigDecimal(wishes.get(position).price * hkdRate).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            "USD" -> priceInPeso = BigDecimal(wishes.get(position).price * usdRate).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            else -> {
                priceInPeso = wishes.get(position).price
            }
        }
        when(priceInPeso){
            in 1.00..499.99 -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
            in 500.00..999.99 -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFDFBA"))
            else -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
        }


        holder?.view?.textView_mainTitle?.text = wishes.get(position).name
        holder?.view?.textView_description?.text = "$priceInPeso PHP - ${wishes.get(position).price} ${wishes.get(position).curr}"
        holder?.view?.textView_deadline?.text = "${deadline.month.value}/${deadline.dayOfMonth}"

        val db = EducateDBHandler(holder.view.context)

        holder?.itemView.setOnLongClickListener{

            //CHOOSE TEMPLATE FOR EACH FRAME
            val dialog = AlertDialog.Builder(holder.view.context)
            val layoutInflater = LayoutInflater.from(holder.view.context)
            val dialogView = layoutInflater.inflate(R.layout.add_choose_dialog,null)

            //LOAD CURRENCY CHOICES FOR SPINNER
            val spinnerCurrency = dialogView.findViewById<Spinner>(R.id.spinner_currency)
            val currencies = arrayListOf<String>("HKD","USD","PHP")
            val currAdapter = ArrayAdapter<String>(holder.view.context, android.R.layout.simple_spinner_dropdown_item,currencies)
            spinnerCurrency.adapter = currAdapter

            //SET TEXT WITHIN FRAME
            dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit item name"
            dialogView.findViewById<TextView>(R.id.textView_description).text = "Edit item price"
            dialogView.findViewById<EditText>(R.id.editText_title).setText(wishes.get(position).name)
            dialogView.findViewById<EditText>(R.id.editText_description).setText(wishes.get(position).price.toString())
            spinnerCurrency.setSelection(currAdapter.getPosition(wishes.get(position).curr))

            //GET VALUES FROM EDIT TEXT FIELDS
            var entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            var entryDescription = dialogView.findViewById<EditText>(R.id.editText_description).text

            //SETUP VALUES FOR DIALOGVIEW
            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Save Changes", { dialogInterface: DialogInterface, i: Int -> })
            dialog.setNegativeButton("Delete Entry", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()

            //LISTENER FOR EDIT
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{

                if(entryTitle.isNotEmpty()){
                    if(entryDescription.toString().matches("(?<=^| )\\d+(\\.\\d+)?(?=\$| )".toRegex())) {
                        //DAY COUNTER
                        val c =  Calendar.getInstance();
                        val year = c.get(Calendar.YEAR)
                        val month = c.get(Calendar.MONTH)
                        val day = c.get(Calendar.DAY_OF_MONTH)

                        val chosenDate = DatePickerDialog(holder.view.context, DatePickerDialog.OnDateSetListener{ view, choiceYear:Int, choiceMonth:Int, choiceDay:Int ->

                            var dec = DecimalFormat("00")
                            var deadline = "$choiceYear-${dec.format(choiceMonth+1)}-${dec.format(choiceDay)}"

                            //ORIGINAL FUNCTIONS
                            val newWish = Wish(entryTitle.toString(), entryDescription.toString().toDouble(), spinnerCurrency.selectedItem.toString(), deadline)
                            db.updateData(wishes.get(position).id, newWish)
                            this.wishes = db.readData(this.query)
                            this.notifyDataSetChanged()
                            this.notifyItemChanged(position)
                            customDialog.dismiss()
                            //---ORIGINAL FUNCTIONS

                            Toast.makeText(
                                holder.view.context,
                                "Entry ${position + 1} updated.",
                                Toast.LENGTH_SHORT
                            ).show()


                        }, year, month, day)

                        chosenDate.show()


                    }
                }
                else{
                    Toast.makeText(holder.view.context, "Enter a product name!", Toast.LENGTH_LONG).show()
                }
            }

            //LISTENER FOR DELETE
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
                db.deleteData(wishes.get(position).id)
                this.wishes.removeAt(position)
                this.notifyItemRemoved(position)
                this.notifyItemRangeChanged(position, this.wishes.size);
                customDialog.dismiss()
                Toast.makeText(holder.view.context, "Entry ${position + 1} deleted.", Toast.LENGTH_SHORT).show()
            }


            true
        }


    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}