package com.example.myfirstapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_educate.*
import kotlinx.android.synthetic.main.fragment_educate.view.*
import kotlinx.android.synthetic.main.fragment_endure.*
import kotlinx.android.synthetic.main.fragment_explore.*
import okhttp3.*
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
        val thisView = inflater.inflate(R.layout.fragment_educate, container, false)
        return thisView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val db = EducateDBHandler(this.context!!)
        recyclerView_educate.layoutManager = LinearLayoutManager(this.context!!)


        //var url = "https://store.playstation.com/store/api/chihiro/00_09_000/container/US/en/999/UP9000-NPUA80677_00-SOTC000000000001"
        //var url = "https://reqres.in/api/users/2"
        //val db = EducateDBHandler()
        //API
        var url = "https://free.currconv.com/api/v7/convert?q=HKD_PHP&compact=ultra&apiKey=d0bacd4bbe5106fbe9fc"

        var request = Request.Builder().url(url).build()
        var client = OkHttpClient()

        //var mAdapter : EducateRecyclerAdapter
        var mAdapter = EducateRecyclerAdapter()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                println(body)
                val gson = GsonBuilder().create()
                //val contentList = gson.fromJson(body, ContentList::class.java)

                val currentRate = gson.fromJson(body, EducateFragment.ExchangeRate::class.java)
                mAdapter.exchangeRate = currentRate.HKD_PHP

                activity!!.runOnUiThread {


                    recyclerView_educate.adapter = mAdapter
                    //recyclerView_educate.adapter = mAdapter
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("call failed")
            }
        })


        mAdapter.entries = db.readData()


        view.addButton.setOnClickListener{

            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog,null)

            val entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            val entryDescription = dialogView.findViewById<EditText>(R.id.editText_description)
            dialogView.findViewById<TextView>(R.id.textView_description).text = "Price (in HKD)"
            entryDescription.hint = "Enter the item price (in HKD)"

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Add", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{

                if(entryTitle.toString().isNotEmpty()){
                    //println(entryDescription.toString())
                    if(entryDescription.text.toString().matches("(?<=^| )\\d+(\\.\\d+)?(?=\$| )".toRegex())) {
                        val newEntry =
                            Entry(entryTitle.toString(), entryDescription.text.toString())
                        db.insertData(newEntry)
                        mAdapter.entries = db.readData()
                        customDialog.dismiss()
                    }
                    else{
                        Toast.makeText(this.context, "Enter only numbers.", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this.context, "Enter a title!", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    class ExchangeRate(val HKD_PHP: Double)
    //class ContentList(val data: List<Data>)
    //class ContentSingle(val data: Data)
    //class Data(val id: Int, val email: String, val first_name: String, val last_name: String, val avatar: String)

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