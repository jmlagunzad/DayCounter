package com.example.myfirstapp.Views.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
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


class EvolveRecyclerAdapter(view: View): RecyclerView.Adapter<CustomViewHolder>() {

    var logs : MutableList<Log> = ArrayList()
    private val presenter = EvolveRecyclerAdapterPresenter(view)
    var evolveActionMode: ActionMode? = null
    val selectedItemsPosition = mutableListOf<Int>()
    val selectedItemsId = mutableListOf<Int>()

    override fun getItemCount(): Int {
        return logs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    fun weightInKg(position: Int): Double{
        return if(logs.get(position).unit == "KG") logs.get(position).value
        else presenter.displayConversion(logs.get(position).value,logs.get(position).unit, false).toDouble()
    }

    fun selectItem(position: Int){
        if(logs.get(position).selected){
            selectedItemsPosition.remove(position)
            selectedItemsId.remove(logs.get(position).id)
        }
        else{
            selectedItemsPosition.add(position)
            selectedItemsId.add(logs.get(position).id)
        }

        logs.get(position).selected = !logs.get(position).selected
        this.notifyItemChanged(position)

    }

    fun clearItemLists(){
        selectedItemsPosition.clear()
        selectedItemsId.clear()
    }

    fun resetSelectedItems(){

        if(!selectedItemsPosition.isEmpty()){
            selectedItemsPosition.sorted()
            selectedItemsPosition.forEach{
                logs.get(it).selected = false
            }

            this.notifyItemRangeChanged((selectedItemsPosition[0]), selectedItemsPosition[selectedItemsPosition.lastIndex])

            clearItemLists()
        }

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var difference = 0.0
        var differenceText = ""

        if(position > 0){
            difference = presenter.computeDifference(weightInKg(position-1), weightInKg(position))

            if(difference < 0){
                holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
                differenceText = " [+" + (difference * -1) + " KG]"
            }
            else{
                holder.view.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
                differenceText = " [-" + difference + " KG]"
            }
        }
        else{
            holder.view.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
        }

        if(logs.get(position).selected){
            holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        holder.view.textView_mainTitle?.text = logs.get(position).title + differenceText
        holder.view.textView_description?.text = presenter.displayConversion(logs.get(position).value,logs.get(position).unit)
        holder.view.textView_deadline?.text = logs.get(position).log_date

        holder.itemView.setOnLongClickListener{

            if(evolveActionMode != null){
                selectItem(position)
                Toast.makeText(holder.view.context, "Clicked "+ position + "selected: " + logs.get(position).selected, Toast.LENGTH_SHORT).show()
            }
            else{
                evolveActionMode = (holder.view.context as AppCompatActivity?)!!.startSupportActionMode(object : ActionMode.Callback{
                    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        mode!!.menuInflater.inflate(R.menu.educate_menu,menu)
                        mode.setTitle("Choose option")
                        menu!!.findItem(R.id.item3).isEnabled = false
//                view.findViewById<ViewPager2>(R.id.educateViewPager).visibility = View.INVISIBLE
                        return true
                    }

                    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        return false
                    }

                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                        when(item!!.itemId){
                            R.id.item_select_all -> {
//                                selectAll()
                        Toast.makeText(
                            holder.view.context,
                            "Select all",
                            Toast.LENGTH_SHORT
                        ).show()
                        mode!!.finish()
                                return true
                            }
                            R.id.item_delete -> {
//                                deleteSelectedItems()
                                Toast.makeText(
                                    holder.view.context,
                                    "Entries deleted!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                mode!!.finish()
                                return true
                            }
                            else -> return false
                        }
                    }

                    override fun onDestroyActionMode(mode: ActionMode?) {
                        resetSelectedItems()
                        notifyDataSetChanged()
                        evolveActionMode = null
//                        toggleButtons()
                    }
                })
                selectItem(position)
                Toast.makeText(holder.view.context, "Clicked "+ position + "selected: " + logs.get(position).selected, Toast.LENGTH_SHORT).show()
//                toggleButtons()
            }

//            Log.e("MainActivity", "clicked. ")
//            Toast.makeText(holder.view.context, "Clicked "+ position + "!", Toast.LENGTH_LONG).show()
            true
        }

        holder.itemView.setOnClickListener{

            if(evolveActionMode != null){
                selectItem(position)
                Toast.makeText(holder.view.context, "Clicked "+ position + "selected: " + logs.get(position).selected, Toast.LENGTH_SHORT).show()

            }
            else {


                //CHOOSE TEMPLATE FOR EACH FRAME
                val dialog = AlertDialog.Builder(holder.view.context)
                val layoutInflater = LayoutInflater.from(holder.view.context)
                val dialogView = layoutInflater.inflate(R.layout.add_choose_dialog, null)

                //SET TEXT WITHIN FRAME
                dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit log"
                dialogView.findViewById<TextView>(R.id.textView_title).text = "Title"
                dialogView.findViewById<TextView>(R.id.textView_description).text = "Value"
                dialogView.findViewById<EditText>(R.id.editText_title)
                    .setText(logs.get(position).title)
                dialogView.findViewById<EditText>(R.id.editText_description)
                    .setText(logs.get(position).value.toString())

                //LOAD UNIT CHOICES FOR SPINNER
                val unitSpinner = dialogView.findViewById<Spinner>(R.id.spinner_currency)
                val units = arrayListOf("KG", "LBS")
                val unitAdapter = ArrayAdapter<String>(
                    holder.view.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    units
                )
                unitSpinner.adapter = unitAdapter
                unitSpinner.setSelection(unitAdapter.getPosition(logs.get(position).unit))

                unitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    var itemPosition = holder.adapterPosition
                    var initialSelect = ""

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (initialSelect == "") {
                            dialogView.findViewById<EditText>(R.id.editText_description)
                                .setText(logs.get(itemPosition).value.toString())
                        } else {
                            dialogView.findViewById<EditText>(R.id.editText_description).setText(
                                presenter.displayConversion(
                                    dialogView.findViewById<EditText>(R.id.editText_description).text.toString()
                                        .toDouble(),
                                    initialSelect,
                                    false
                                )
                            )
                        }
                        initialSelect = unitSpinner.selectedItem.toString()
                    }
                }


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
                customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                    //GET VALUES FROM EDIT TEXT FIELDS
                    val logTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
                    val logValue = dialogView.findViewById<EditText>(R.id.editText_description).text

                    try {

                        logs = presenter.updateTransaction(
                            logTitle.toString(),
                            logValue.toString(),
                            unitSpinner.selectedItem.toString(),
                            logs.get(position).id
                        )!!
//                    this.notifyDataSetChanged()
//                    this.notifyItemChanged(position)
                        this.notifyItemRangeChanged(position, itemCount)
                        customDialog.dismiss()

                        Toast.makeText(
                            holder.view.context,
                            "Entry ${position + 1} updated.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        println(e.toString())
                        Toast.makeText(
                            holder.view.context,
                            "Enter a proper value",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                //LISTENER FOR DELETE
                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                    presenter.deleteRecord(logs.get(position).id)
                    this.logs.removeAt(position)
                    this.notifyItemRemoved(position)
                    this.notifyItemRangeChanged(position, this.logs.size)

                    customDialog.dismiss()
                    Toast.makeText(
                        holder.view.context,
                        "Entry ${position + 1} deleted.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            true



        }
    }



}