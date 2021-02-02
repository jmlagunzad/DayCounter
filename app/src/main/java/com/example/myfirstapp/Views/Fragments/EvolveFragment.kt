package com.example.myfirstapp.Views.Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstapp.Handlers.EducateDBHandler
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.Presenter.EvolvePresenter
import com.example.myfirstapp.R
import com.example.myfirstapp.Views.Adapters.EducateRecyclerAdapter
import com.example.myfirstapp.Views.Adapters.EvolveRecyclerAdapter
import com.example.myfirstapp.Views.Adapters.ExploreRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_educate.view.*
import kotlinx.android.synthetic.main.fragment_educate.view.addButton
import kotlinx.android.synthetic.main.fragment_evolve.view.*
import kotlinx.android.synthetic.main.fragment_explore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EvolveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EvolveFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_evolve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val evolvePresenter = EvolvePresenter(view)
        val mAdapter = EvolveRecyclerAdapter()
        //val db = EducateDBHandler(this.context!!)

        recyclerView.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView.adapter = mAdapter

        mAdapter.logs = evolvePresenter.getLogs()

        view.evolve_addButton.setOnClickListener {
            //educatePresenter.test()
            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_choose_dialog, null)
            val logTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            val logAmount = dialogView.findViewById<EditText>(R.id.editText_description).text
            val unitSpinner = dialogView.findViewById<Spinner>(R.id.spinner_currency)

            //LOAD UNIT CHOICES FOR SPINNER
            val units = arrayListOf("KG","LBS")
            val unitAdapter = ArrayAdapter<String>(this.context!!, android.R.layout.simple_spinner_dropdown_item,units)
            unitSpinner.adapter = unitAdapter

            dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Add new log"

            dialogView.findViewById<TextView>(R.id.textView_title).text = "Title"
            dialogView.findViewById<EditText>(R.id.editText_title).hint = "Enter log title"

            dialogView.findViewById<TextView>(R.id.textView_description).text = "Value"
            dialogView.findViewById<EditText>(R.id.editText_description).hint = "Enter weight value"

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Add", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()

            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                if(evolvePresenter.addLog(logTitle.toString(), logAmount.toString(), unitSpinner.selectedItem.toString())){
                    mAdapter.logs = evolvePresenter.getLogs()
                    mAdapter.notifyItemInserted(mAdapter.logs.size)
                    mAdapter.notifyDataSetChanged()

                    customDialog.dismiss()
                }

                else{
                    Toast.makeText(view.context, "Enter a proper numeric value", Toast.LENGTH_LONG).show()
                }

            }



        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EvolveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EvolveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}