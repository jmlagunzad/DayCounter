package com.example.myfirstapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_explore.*
import okhttp3.*
import java.io.IOException
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mAdapter = ExploreRecyclerAdapter()
        val db = EducateDBHandler(this.context!!)

        recyclerView.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView.adapter = mAdapter

        mAdapter.wishes = db.readData()

        var url =
            "https://free.currconv.com/api/v7/convert?q=HKD_PHP,USD_PHP&compact=ultra&apiKey=d0bacd4bbe5106fbe9fc"
        var request = Request.Builder().url(url).build()
        var client = OkHttpClient()
        //var mAdapter = EducateRecyclerAdapter(activity!!)


        //API GET REQUEST FOR CURRENCY CONVERTER
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                println(body)
                val gson = GsonBuilder().create()
                //val contentList = gson.fromJson(body, ContentList::class.java)
                try{
                    val currentRate = gson.fromJson(body, EducateFragment.ExchangeRate::class.java)
                    activity!!.runOnUiThread {
                        mAdapter.hkdRate = currentRate.HKD_PHP
                        mAdapter.usdRate = currentRate.USD_PHP
                        mAdapter.notifyDataSetChanged()

                    }
                }
                catch(e: Exception){
                    println("Could not get live data for currency exchange rates.")
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("call failed")
            }
        })

        addButton.setOnClickListener{

            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_choose_dialog,null)

            val entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            val entryDescription = dialogView.findViewById<EditText>(R.id.editText_description).text
            val spinnerCurrency = dialogView.findViewById<Spinner>(R.id.spinner_currency)

            //LOAD CURRENCY CHOICES FOR SPINNER
            val currencies = arrayListOf("HKD","USD","PHP")
            val currAdapter = ArrayAdapter<String>(this.context!!, android.R.layout.simple_spinner_dropdown_item,currencies)
            spinnerCurrency.adapter = currAdapter

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Add", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{

                if(entryTitle.toString().isNotEmpty()){


                    //DAY COUNTER
                    val c =  Calendar.getInstance();
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)

                    val chosenDate = DatePickerDialog(this.context!!, DatePickerDialog.OnDateSetListener{ view, choiceYear:Int, choiceMonth:Int, choiceDay:Int ->


                        var dec = DecimalFormat("00")
                        var deadline = "$choiceYear-${dec.format(choiceMonth+1)}-${dec.format(choiceDay)}"

                        //ORIGINAL FUNCTIONS
                        val newWish = Wish(entryTitle.toString(), entryDescription.toString().toDouble(), spinnerCurrency.selectedItem.toString(), deadline)
                        db.insertData(newWish)
                        mAdapter.wishes = db.readData()
                        customDialog.dismiss()
                        //---ORIGINAL FUNCTIONS


                    }, year, month, day)

                    chosenDate.show()
                    customDialog.dismiss()

                }
                else{

                    Toast.makeText(this.context, "Enter a title!", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

}