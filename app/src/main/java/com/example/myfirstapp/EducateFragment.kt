package com.example.myfirstapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_educate.*
import kotlinx.android.synthetic.main.fragment_educate.view.*
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
        val thisView = inflater.inflate(R.layout.fragment_educate, container, false)
        return thisView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //INITIALIZATIONS
        val db = EducateDBHandler(this.context!!)
        recyclerView_educate.layoutManager = LinearLayoutManager(this.context!!)
        var url =
            "https://free.currconv.com/api/v7/convert?q=HKD_PHP&compact=ultra&apiKey=d0bacd4bbe5106fbe9fc"
        var request = Request.Builder().url(url).build()
        var client = OkHttpClient()
        var mAdapter = EducateRecyclerAdapter(activity!!)


        //API GET REQUEST FOR CURRENCY CONVERTER
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                println(body)
                val gson = GsonBuilder().create()
                //val contentList = gson.fromJson(body, ContentList::class.java)

                val currentRate = gson.fromJson(body, EducateFragment.ExchangeRate::class.java)
                mAdapter.exchangeRate = currentRate.HKD_PHP

                activity!!.runOnUiThread {


                    recyclerView_educate.adapter = mAdapter
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("call failed")
            }
        })

        fun refreshList() {
            //API GET REQUEST FOR LOCALHOST WISHES API
            url = "http://192.168.1.2:5000/api/v1/resources/wishes/all"
            request =
                Request.Builder().url(url).addHeader("Content-Type", "application/json").build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response?.body?.string()

                    //println(response?.body?.string())
                    val gson = GsonBuilder().create()
                    val wishes = gson.fromJson(body, Array<Wish>::class.java).toMutableList()



                    activity!!.runOnUiThread {
                        mAdapter.wishes = wishes
                        mAdapter.notifyDataSetChanged()

                    }

                }

                override fun onFailure(call: Call, e: IOException) {
                    println("call failed")

                }
            })
        }

        refreshList()


        view.addButton.setOnClickListener{

            val dialog = AlertDialog.Builder(this.context!!)
            val dialogView = layoutInflater.inflate(R.layout.add_choose_dialog,null)

            val entryTitle = dialogView.findViewById<EditText>(R.id.editText_title).text
            val entryDescription = dialogView.findViewById<EditText>(R.id.editText_description).text
            val spinnerCurrency = dialogView.findViewById<Spinner>(R.id.spinner_currency)

            //LOAD CURRENCY CHOICES FOR SPINNER
            val currencies = arrayListOf<String>("HKD","USD","PHP")
            val currAdapter = ArrayAdapter<String>(this.context!!, android.R.layout.simple_spinner_dropdown_item,currencies)
            spinnerCurrency.adapter = currAdapter

            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Add", { dialogInterface: DialogInterface, i: Int -> })
            val customDialog = dialog.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{

                if(entryTitle.toString().isNotEmpty()){
                    //println(entryDescription.toString())
                    if(entryDescription.toString().matches("(?<=^| )\\d+(\\.\\d+)?(?=\$| )".toRegex())) {

                        //mAdapter.entries = db.readData()
                        var url = "http://192.168.1.2:5000/api/v1/resources/wishes/new"

                        val payload = JSONObject("""{"title": "${entryTitle}","price": "${entryDescription}"}""").toString()
                        val requestBody = payload.toRequestBody()

                        var request = Request.Builder().method("POST", requestBody).url(url).build()
                        var client = OkHttpClient()

                        client.newCall(request).enqueue(object: Callback {
                            override fun onResponse(call: Call, response: Response) {
                                val body = response?.body?.string()

                                println(body)


                                refreshList()

                            }

                            override fun onFailure(call: Call, e: IOException) {
                                println("call failed")
                            }
                        })



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
    //class WishListsList(val wishLists: List<WishList>)
    class WishList(val wishes: Array<Wish>)
    class Wish(val id: Int, val price: Double, val title:String)


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