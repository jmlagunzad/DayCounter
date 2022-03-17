package com.example.myfirstapp.Views.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.Model.Transaction
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.Presenter.EducateRecyclerAdapterPresenter
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.educate_row.view.layout_background
import kotlinx.android.synthetic.main.educate_row.view.textView_deadline
import kotlinx.android.synthetic.main.educate_row_v2.view.*
import kotlinx.android.synthetic.main.fragment_educate.view.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle


class EducateRecyclerAdapter(view: View, listener: EducatePresenter.OnEditOrDelete): RecyclerView.Adapter<EducateViewHolder>() {

    private val listener = listener
    private var educateActionMode: ActionMode? = null

    val constantFilters = mutableListOf("ALL","THIS CUTOFF","LAST CUTOFF", "THIS MONTH", "LAST MONTH")
    var transactions: MutableList<Transaction> = ArrayList()
    val adapterPresenter = EducateRecyclerAdapterPresenter(view)
    val view = view
    val selectedItemsPosition = mutableListOf<Int>()
    val selectedItemsId = mutableListOf<Int>()

    var mRecyclerView: RecyclerView? = null


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        if (transactions.size > 21){
            if(view.findViewById<Spinner>(R.id.spinner_filterCategory).selectedItem.toString() == "ALL"){
                return 20
            }
        }

        return transactions.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row_v2, parent, false)
        return EducateViewHolder(cellForRow)
    }

    fun selectItem(position: Int, id: Int): Boolean{
        if(selectedItemsPosition.contains(position)){
            selectedItemsPosition.remove(position)
            selectedItemsId.remove(id)
            return false
        }
        else{
            selectedItemsPosition.add(position)
            selectedItemsId.add(id)
            return true
        }
    }

    fun selectItemHolder(holder: EducateViewHolder, position: Int, id: Int): Boolean{

        if(selectedItemsPosition.contains(position)){
            selectedItemsPosition.remove(position)
            selectedItemsId.remove(id)
            if(transactions.get(position).amount < 0.0){
                holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
            }
            else{
                holder.view.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
            }
            return false
        }
        else{
            selectedItemsPosition.add(position)
            selectedItemsId.add(id)
            holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFFFFF"))
            return true
        }
    }

    fun selectAll(){
//        for (var childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
//            final ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
//
//        }

        for(ctr in 0..mRecyclerView!!.childCount){
//            val child = mRecyclerView!!.getChildAt(ctr)
//            val holder = mRecyclerView!!.findViewHolderForLayoutPosition(ctr);
//                mRecyclerView!!.findViewHolderForAdapterPosition(ctr) as EducateViewHolder
//            recyclerView!!.getChildViewHolder(recyclerView!!.getChildAt(ctr)) as EducateViewHolder
//            selectItemHolder(
//                holder as EducateViewHolder,
//                ctr,
//                transactions.get(ctr).id)
            Log.e("MainActivity", ctr.toString())
        }

    }

    fun deleteSelectedItems(){
//        var entryCategory =
//            dialogView.findViewById<EditText>(R.id.editText_category).text

        adapterPresenter.deleteTransactions(selectedItemsId)
//        for(position in selectedItemsPosition..)
        selectedItemsPosition.sortDescending()
        selectedItemsPosition.forEach{
            this.transactions.removeAt(it)
            this.notifyItemRemoved(it)
        }

        this.notifyItemRangeChanged(selectedItemsPosition[selectedItemsPosition.lastIndex], this.transactions.size);
        listener.recompute(adapterPresenter.computeBalance(transactions))
        listener.refreshSpinner(
            constantFilters.plus(adapterPresenter.getCategories()),
            listener.getCurrentFilter()
        )
    }

    fun resetSelectedItems(){
        selectedItemsPosition.clear()
        selectedItemsId.clear()
    }

    fun toggleButtons(){
        if(educateActionMode != null){
            view.addButton.visibility = View.INVISIBLE
            view.minusButton.visibility = View.INVISIBLE
            view.exportButton.visibility = View.INVISIBLE
            view.importButton.visibility = View.INVISIBLE
        }
        else{
            view.addButton.visibility = View.VISIBLE
            view.minusButton.visibility = View.VISIBLE
            view.exportButton.visibility = View.VISIBLE
            view.importButton.visibility = View.VISIBLE
        }


    }

    override fun onBindViewHolder(holder: EducateViewHolder, position: Int) {
        holder.view.textView_mainTitle?.text = transactions.get(position).title
        holder.view.textView_description?.text = transactions.get(position).amount.toString()
        if(transactions.get(position).amount < 0.0){
            holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
        }
        else{
            holder.view.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
        }
        holder.view.textView_deadline?.text = transactions.get(position).transaction_date
        holder.view.switch_active.isChecked = transactions.get(position).active
        holder.view.textView_category.text = transactions.get(position).category

        holder.view.switch_active.setOnCheckedChangeListener{ _ , isChecked ->
            listener.recomputePair(adapterPresenter.tempBalance(transactions, isChecked, position))
        }

        class callback : ActionMode.Callback{

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode!!.menuInflater.inflate(R.menu.educate_menu,menu)
                mode.setTitle("Choose option")
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                when(item!!.itemId){
                    R.id.item_select_all -> {
                        selectAll()
                        Toast.makeText(
                            holder.view.context,
                            "Select all",
                            Toast.LENGTH_SHORT
                        ).show()
                        mode!!.finish()
                        return true
                    }
                    R.id.item_delete -> {
                        deleteSelectedItems()
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
                educateActionMode = null
                toggleButtons()
            }

        }

        holder.itemView.setOnLongClickListener{
            //Change color when selected
//            transactions.get(position).selected = !transactions.get(position).selected
//            if(transactions.get(position).selected){
//                holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            }
//            else{
//                if(transactions.get(position).amount < 0.0){
//                    holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
//                }
//                else{
//                    holder.view.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
//                }
//            }

            if(educateActionMode != null){
                selectItemHolder(holder,position,transactions.get(position).id)
            }
            else{
                educateActionMode = (view.context as AppCompatActivity?)!!.startSupportActionMode(callback())
                selectItemHolder(holder,position,transactions.get(position).id)
                toggleButtons()
            }

//            Log.e("MainActivity", "clicked. ")
//            Toast.makeText(holder.view.context, "Clicked "+ position + "!", Toast.LENGTH_LONG).show()
            true
        }


        holder.itemView.setOnClickListener{

            if(educateActionMode != null){
                selectItemHolder(holder,position,transactions.get(position).id)
//                transactions.get(position).selected = !transactions.get(position).selected
//                if(selectItem(position,transactions.get(position).id)){
//                    holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFFFFF"))
//                }
//                else{
//                    if(transactions.get(position).amount < 0.0){
//                        holder.view.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
//                    }
//                    else{
//                        holder.view.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
//                    }
//                }


                Toast.makeText(holder.view.context, selectedItemsPosition.joinToString(separator = ", "), Toast.LENGTH_SHORT).show()
            }
            else{


                //CHOOSE TEMPLATE FOR EACH FRAME
                val dialog = AlertDialog.Builder(holder.view.context)
                val layoutInflater = LayoutInflater.from(holder.view.context)
                val dialogView = layoutInflater.inflate(R.layout.add_dialog_v2, null)

                //SET TEXT WITHIN FRAME
                dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit item name"
                dialogView.findViewById<TextView>(R.id.textView_description).text =
                    "Edit item price"
                dialogView.findViewById<EditText>(R.id.editText_title)
                    .setText(transactions.get(position).title)
                dialogView.findViewById<EditText>(R.id.editText_description)
                    .setText(transactions.get(position).amount.toString())
                dialogView.findViewById<EditText>(R.id.editText_category)
                    .setText(transactions.get(position).category)

                //LOAD CATEGORIES FOR SPINNER
                val categorySpinner = dialogView.findViewById<Spinner>(R.id.spinner_category)
                val categories = mutableListOf("").plus(adapterPresenter.getCategories())
                val categoryAdapter = ArrayAdapter<String>(
                    holder.view.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    categories
                )
                categorySpinner.adapter = categoryAdapter
                categorySpinner.setSelection(categoryAdapter.getPosition(transactions.get(position).category))

                categorySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            dialogView.findViewById<EditText>(R.id.editText_category)
                                .setText(categorySpinner.selectedItem.toString())
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
                    var entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
                    var entryDescription =
                        dialogView.findViewById<EditText>(R.id.editText_description).text
                    var entryCategory =
                        dialogView.findViewById<EditText>(R.id.editText_category).text

                    try {

                        transactions = adapterPresenter.updateTransaction(
                            entryTitle.toString(),
                            entryDescription.toString(),
                            entryCategory.toString(),
                            transactions.get(position).id
                        )!!
                        this.notifyDataSetChanged()
                        this.notifyItemChanged(position)
                        listener.recompute(adapterPresenter.computeBalance(transactions))
                        listener.refreshSpinner(
                            constantFilters.plus(adapterPresenter.getCategories()),
                            listener.getCurrentFilter()
                        )
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
                            "Enter a proper amount",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                //LISTENER FOR DELETE
                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                    var entryCategory =
                        dialogView.findViewById<EditText>(R.id.editText_category).text

                    adapterPresenter.deleteTransaction(transactions.get(position).id)
                    this.transactions.removeAt(position)
                    this.notifyItemRemoved(position)
                    this.notifyItemRangeChanged(position, this.transactions.size);
                    listener.recompute(adapterPresenter.computeBalance(transactions))
                    listener.refreshSpinner(
                        constantFilters.plus(adapterPresenter.getCategories()),
                        listener.getCurrentFilter()
                    )
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

class EducateViewHolder(val view: View): RecyclerView.ViewHolder(view){

}