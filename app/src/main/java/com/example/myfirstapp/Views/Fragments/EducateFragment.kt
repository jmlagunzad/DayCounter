package com.example.myfirstapp.Views.Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.R
import com.example.myfirstapp.Views.Adapters.EducateRecyclerAdapter
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.fragment_educate.*
import kotlinx.android.synthetic.main.fragment_educate.view.*

import com.opencsv.CSVWriter
import android.database.sqlite.SQLiteDatabase
import android.os.Environment

import java.io.File
import java.io.FileWriter
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EducateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EducateFragment : Fragment(), EducatePresenter.OnEditOrDelete{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var balance = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_educate, container, false)
    }

    override fun recompute(computed: Double) {
        view!!.findViewById<TextView>(R.id.textView_balance).text = computed.toString()
        view!!.findViewById<TextView>(R.id.textView_difference).text = ""
    }

    override fun recomputePair(computed: Pair<Double, Double>) {
        var (total, difference) = computed
        difference *= -1
        view!!.findViewById<TextView>(R.id.textView_balance).text = total.toString()
        view!!.findViewById<TextView>(R.id.textView_difference).text = difference.toString()

        if(difference < 0.0){
            view!!.findViewById<TextView>(R.id.textView_difference).setTextColor(Color.parseColor("#f44336"))
        }
        else{
            view!!.findViewById<TextView>(R.id.textView_difference).setTextColor(Color.parseColor("#646B70"))
        }
    }

    override fun refreshSpinner(spinner: Spinner, categories: List<String>, currentCategory: String){
        //Load categories for filtering
        //val categories = mutableListOf("ALL").plus(educatePresenter.getCategories())

        val categoryAdapter = ArrayAdapter<String>(view!!.context, android.R.layout.simple_spinner_dropdown_item,categories)
        spinner.adapter = categoryAdapter
        val currentFilter = categoryAdapter.getPosition(currentCategory)
        //println(currentFilter)
        if(currentFilter == -1){
            spinner.setSelection(categoryAdapter.getPosition("ALL"))
        }
        else{
            spinner.setSelection(categoryAdapter.getPosition(currentCategory))
        }
    }

    override fun refreshSpinner(categories: List<String>, currentCategory: String){
        //Load categories for filtering
        //val categories = mutableListOf("ALL").plus(educatePresenter.getCategories())

        val categoryAdapter = ArrayAdapter<String>(view!!.context, android.R.layout.simple_spinner_dropdown_item,categories)
        val filterSpinner = view!!.findViewById<Spinner>(R.id.spinner_filterCategory)
        filterSpinner.adapter = categoryAdapter
        val currentFilter = categoryAdapter.getPosition(currentCategory)
        //println(currentFilter)
        if(currentFilter == -1){
            filterSpinner.setSelection(categoryAdapter.getPosition("ALL"))
        }
        else{
            filterSpinner.setSelection(categoryAdapter.getPosition(currentCategory))
        }
    }

    override fun getCurrentFilter(): String {
        //println(view!!.findViewById<Spinner>(R.id.spinner_filterCategory).selectedItem.toString())
        return view!!.findViewById<Spinner>(R.id.spinner_filterCategory).selectedItem.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Init values
        val constantFilters = mutableListOf("ALL","THIS CUTOFF","LAST CUTOFF", "THIS MONTH", "LAST MONTH")
        val typeFilters = mutableListOf("ALL", "INCOME", "EXPENSES")

        val educatePresenter = EducatePresenter(view)
        val educateRecyclerAdapter = EducateRecyclerAdapter(view, this)
        val filterSpinner = view!!.findViewById<Spinner>(R.id.spinner_filterCategory)

        val typeSpinner = view!!.findViewById<Spinner>(R.id.spinner_filterType)

        recyclerView_educate.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_educate.adapter = educateRecyclerAdapter

        //Prevent recyclerview from reloading on scroll
        recyclerView_educate.getRecycledViewPool().setMaxRecycledViews(0, 0)

        //Get initial category list
        refreshSpinner(filterSpinner,constantFilters.plus(educatePresenter.getCategories()),"ALL")
        refreshSpinner(typeSpinner, typeFilters, "ALL")

        //Get latest transactions and categories
        educateRecyclerAdapter.transactions = educatePresenter.getTransactions("ALL","ALL")

        //Get current balance
        recompute(educatePresenter.computeBalance(educateRecyclerAdapter.transactions))

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var filter = filterSpinner.selectedItem.toString()
                var type = typeSpinner.selectedItem.toString()
//                if(filter == "ALL"){
//                    educateRecyclerAdapter.transactions = educatePresenter.getTransactions()
//                }
//                else{
                    educateRecyclerAdapter.transactions = educatePresenter.getTransactions(filter,type)
               // }
                educateRecyclerAdapter.notifyDataSetChanged();
                recompute(educatePresenter.computeBalance(educateRecyclerAdapter.transactions))
            }
        }

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var filter = filterSpinner.selectedItem.toString()
                var type = typeSpinner.selectedItem.toString()
//                if(filter == "ALL"){
//                    educateRecyclerAdapter.transactions = educatePresenter.getTransactions()
//                }
//                else{
                educateRecyclerAdapter.transactions = educatePresenter.getTransactions(filter,type)
                // }
                educateRecyclerAdapter.notifyDataSetChanged();
                recompute(educatePresenter.computeBalance(educateRecyclerAdapter.transactions))
            }
        }

        view.addButton.setOnClickListener {
            //educatePresenter.test()
            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog_v2, null)

            //LOAD CATEGORIES FOR SPINNER
            val categorySpinner = dialogView.findViewById<Spinner>(R.id.spinner_category)
            val categories = mutableListOf("").plus(educatePresenter.getCategories())
            val categoryAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item,categories)
            categorySpinner.adapter = categoryAdapter
            categorySpinner.setSelection(categoryAdapter.getPosition(""))

            dialogView.findViewById<TextView>(R.id.textView_description).text = "Amount Added"
            dialogView.findViewById<TextView>(R.id.textView_description).hint = "Enter amount added"

            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    dialogView.findViewById<EditText>(R.id.editText_category).setText(categorySpinner.selectedItem.toString())
                }
            }

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Add", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()

            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
                val entryAmount = dialogView.findViewById<EditText>(R.id.editText_description).text
                val entryCategory = dialogView.findViewById<EditText>(R.id.editText_category).text

                if(educatePresenter.addTransaction(entryTitle.toString(), entryAmount.toString(), entryCategory.toString())){
                    educateRecyclerAdapter.transactions = educatePresenter.getTransactions()

                    educateRecyclerAdapter.notifyItemInserted(educateRecyclerAdapter.transactions.size)
                    educateRecyclerAdapter.notifyDataSetChanged()

                    recompute(educatePresenter.computeBalance(educateRecyclerAdapter.transactions))
                    refreshSpinner(filterSpinner,constantFilters.plus(educatePresenter.getCategories()),entryCategory.toString())
                    refreshSpinner(typeSpinner, typeFilters, "ALL")
                    customDialog.dismiss()
                }

                else{
                    Toast.makeText(view.context, "Enter a proper amount", Toast.LENGTH_LONG).show()
                }

            }



        }

        view.minusButton.setOnClickListener {
            //educatePresenter.test()
            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog_v2, null)

            //LOAD CATEGORIES FOR SPINNER
            val categorySpinner = dialogView.findViewById<Spinner>(R.id.spinner_category)
            val categories = mutableListOf("").plus(educatePresenter.getCategories())
            val categoryAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item,categories)
            categorySpinner.adapter = categoryAdapter
            categorySpinner.setSelection(categoryAdapter.getPosition(""))

            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    dialogView.findViewById<EditText>(R.id.editText_category).setText(categorySpinner.selectedItem.toString())
                }
            }

            dialogView.findViewById<TextView>(R.id.textView_description).text = "Amount Deducted"
            dialogView.findViewById<TextView>(R.id.textView_description).hint = "Enter amount used"

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Deduct", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()

            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
                val entryAmount = dialogView.findViewById<EditText>(R.id.editText_description).text
                val entryCategory = dialogView.findViewById<EditText>(R.id.editText_category).text

                if(educatePresenter.addNegativeTransaction(
                        entryTitle.toString(),
                        entryAmount.toString(),
                        entryCategory.toString()
                    )){
                    educateRecyclerAdapter.transactions = educatePresenter.getTransactions()

                    educateRecyclerAdapter.notifyItemInserted(educateRecyclerAdapter.transactions.size)
                    educateRecyclerAdapter.notifyDataSetChanged()

                    recompute(educatePresenter.computeBalance(educateRecyclerAdapter.transactions))
                    refreshSpinner(filterSpinner,constantFilters.plus(educatePresenter.getCategories()), entryCategory.toString())
                    refreshSpinner(typeSpinner, typeFilters, "ALL")
                    customDialog.dismiss()
                }

                else{
                    Toast.makeText(view.context, "Enter a proper amount", Toast.LENGTH_LONG).show()
                }
            }



        }

        view.exportButton.setOnClickListener {
//            Toast.makeText(view.context, "Enter a proper amount", Toast.LENGTH_LONG).show()
//            val dbhelper = DBHelper(ApplicationProvider.getApplicationContext())
//            val exportDir = File(Environment.getExternalStorageDirectory(), "")
//            if (!exportDir.exists()) {
//                exportDir.mkdirs()
//            }
//
//            val file = File(exportDir, "csvname.csv")
//            try {
//                file.createNewFile()
//                val csvWrite = CSVWriter(FileWriter(file))
////                val db: SQLiteDatabase = dbhelper.getReadableDatabase()
//                val curCSV: Cursor = educatePresenter.getTransactions()
//                csvWrite.writeNext(curCSV.getColumnNames())
//                while (curCSV.moveToNext()) {
//                    //Which column you want to exprort
//                    val arrStr = arrayOf<String>(
//                        curCSV.getString(0),
//                        curCSV.getString(1),
//                        curCSV.getString(2)
//                    )
//                    csvWrite.writeNext(arrStr)
//                }
//                csvWrite.close()
//                curCSV.close()
//            } catch (sqlEx: Exception) {
//                Log.e("MainActivity", sqlEx.message, sqlEx)
//            }

        }




    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EducateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EducateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}