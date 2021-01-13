package com.example.myfirstapp.Views

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.EducateDBHandler
import com.example.myfirstapp.EducateRecyclerAdapter
import com.example.myfirstapp.ExploreRecyclerAdapter
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_educate.*
import kotlinx.android.synthetic.main.fragment_educate.view.*
import kotlinx.android.synthetic.main.fragment_explore.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EducateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EducateFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_educate, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val educatePresenter = EducatePresenter(view)
        val educateRecyclerAdapter = EducateRecyclerAdapter()

        recyclerView_educate.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_educate.adapter = educateRecyclerAdapter

        educateRecyclerAdapter.entries = educatePresenter.getEntries()

        val currBalance = view.findViewById<TextView>(R.id.textView_balance)

        currBalance.text = educatePresenter.computeBalance(educateRecyclerAdapter.entries).toString()

        view.addButton.setOnClickListener {
            //educatePresenter.test()
            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog, null)
            val entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            val entryAmount = dialogView.findViewById<EditText>(R.id.editText_description).text

            dialogView.findViewById<TextView>(R.id.textView_description).text = "Amount Added"
            dialogView.findViewById<TextView>(R.id.textView_description).hint = "Enter amount added"

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Add", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()

            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                educatePresenter.addEntry(entryTitle.toString(), entryAmount.toString())
                educateRecyclerAdapter.entries = educatePresenter.getEntries()

                educateRecyclerAdapter.notifyItemInserted(educateRecyclerAdapter.entries.size)
                educateRecyclerAdapter.notifyDataSetChanged()

                currBalance.text = educatePresenter.computeBalance(educateRecyclerAdapter.entries).toString()

                customDialog.dismiss()
            }



        }

        view.minusButton.setOnClickListener {
            //educatePresenter.test()
            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog, null)
            val entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            val entryAmount = dialogView.findViewById<EditText>(R.id.editText_description).text

            dialogView.findViewById<TextView>(R.id.textView_description).text = "Amount Deducted"
            dialogView.findViewById<TextView>(R.id.textView_description).hint = "Enter amount used"

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Deduct", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()

            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                educatePresenter.addNegativeEntry(entryTitle.toString(), entryAmount.toString())
                educateRecyclerAdapter.entries = educatePresenter.getEntries()

                educateRecyclerAdapter.notifyItemInserted(educateRecyclerAdapter.entries.size)
                educateRecyclerAdapter.notifyDataSetChanged()

                currBalance.text = educatePresenter.computeBalance(educateRecyclerAdapter.entries).toString()

                customDialog.dismiss()
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