package com.example.myfirstapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.educate_row.view.*
import kotlinx.android.synthetic.main.fragment_educate.*
import kotlinx.android.synthetic.main.fragment_educate.view.*
import kotlinx.android.synthetic.main.frame_row.view.*
import kotlinx.android.synthetic.main.frame_row.view.textView_description
import kotlinx.android.synthetic.main.frame_row.view.textView_mainTitle
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

class EducateRecyclerAdapter(val fa: FragmentActivity?): RecyclerView.Adapter<CustomViewHolder>() {

    //var entries: MutableList<Entry> = ArrayList()
    var wishes: MutableList<EducateFragment.Wish> = ArrayList()
    //var attempts: MutableList<Attempt> = ArrayList()
    var exchangeRate: Double = 0.0
    //var mAdapter = this




    override fun getItemCount(): Int {
        //return entries.size
        //return contents.data.count()
        //return entries.size
        return wishes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var cellForRow = layoutInflater.inflate(R.layout.educate_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var priceInPeso = BigDecimal(wishes.get(position).price * exchangeRate).setScale(2, RoundingMode.HALF_EVEN).toDouble()
        when(priceInPeso){
            in 1.00..499.99 -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#BAFFC9"))
            in 500.00..999.99 -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFDFBA"))
            else -> holder?.view?.layout_background.setBackgroundColor(Color.parseColor("#FFB3BA"))
        }

        holder?.view?.textView_mainTitle?.text = wishes.get(position).title
        holder?.view?.textView_description?.text = "$priceInPeso PHP - ${wishes.get(position).price} HKD"

        //val db = EducateDBHandler(holder.view.context)

        holder?.itemView.setOnLongClickListener{

            val dialog = AlertDialog.Builder(holder.view.context)
            val layoutInflater = LayoutInflater.from(holder.view.context)
            val dialogView = layoutInflater.inflate(R.layout.add_dialog,null)

            dialogView.findViewById<TextView>(R.id.textView_mainTitle).text = "Edit entry"
            dialogView.findViewById<TextView>(R.id.textView_description).text = "Edit price"
            dialogView.findViewById<EditText>(R.id.editText_title).setText(wishes.get(position).title)
            dialogView.findViewById<EditText>(R.id.editText_description).setText(wishes.get(position).price.toString())

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

                    if(entryDescription.toString().matches("(?<=^| )\\d+(\\.\\d+)?(?=\$| )".toRegex())) {


                        //API
                        var url = "http://192.168.1.2:5000/api/v1/resources/wishes/" + wishes.get(position).id

                        val payload = JSONObject("""{"title": "${entryTitle}","price": ${entryDescription}}""").toString()
                        val requestBody = payload.toRequestBody()
                        var request = Request.Builder().method("PUT", requestBody).url(url).build()
                        var client = OkHttpClient()

                        client.newCall(request).enqueue(object: Callback {
                            override fun onResponse(call: Call, response: Response) {
                                val body = response?.body?.string()

                                println(body)

                                //API GET REQUEST FOR LOCALHOST WISHES API
                                url = "http://192.168.1.2:5000/api/v1/resources/wishes/all"
                                request =
                                    Request.Builder().url(url).addHeader("Content-Type", "application/json").build()

                                //client = OkHttpClient()
                                client.newCall(request).enqueue(object : Callback {
                                    override fun onResponse(call: Call, response: Response) {
                                        val body = response?.body?.string()

                                        //println(response?.body?.string())
                                        val gson = GsonBuilder().create()
                                        val wishes2 = gson.fromJson(body, Array<EducateFragment.Wish>::class.java).toMutableList()

                                        fa!!.runOnUiThread{
                                            wishes = wishes2
                                            this@EducateRecyclerAdapter.notifyDataSetChanged()
                                            this@EducateRecyclerAdapter.notifyItemChanged(position)
                                        }
                                    }

                                    override fun onFailure(call: Call, e: IOException) {
                                        println("call failed")
                                    }
                                })
                            }

                            override fun onFailure(call: Call, e: IOException) {
                                println("call failed")
                            }
                        })

                        customDialog.dismiss()

                        Toast.makeText(
                            holder.view.context,
                            "Entry ${position + 1} updated.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        Toast.makeText(holder.view.context, "Enter only numbers.", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(holder.view.context, "Enter a title!", Toast.LENGTH_LONG).show()
                }
            }

            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{

                var url = "http://192.168.1.2:5000/api/v1/resources/wishes/" + wishes.get(position).id
                var request = Request.Builder().delete().url(url).build()
                var client = OkHttpClient()

                client.newCall(request).enqueue(object: Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val body = response?.body?.string()

                        println(body)

                    }

                    override fun onFailure(call: Call, e: IOException) {
                        println("call failed")
                    }
                })

//                db.deleteData(entries.get(position).id)
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