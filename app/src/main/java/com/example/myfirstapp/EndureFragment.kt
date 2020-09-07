package com.example.myfirstapp

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_endure.*
import kotlinx.android.synthetic.main.fragment_explore.*
import org.joda.time.DateTime
import org.joda.time.Days
import java.lang.Math.abs
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.lang.Object

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EndureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EndureFragment : Fragment() {
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
        val thisView = inflater.inflate(R.layout.fragment_endure, container, false)

        return thisView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //RecyclerView
        val mAdapter = EndureRecyclerAdapter()
        val db = EndureDBHandler(this.context!!)

        recyclerView_endure.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView_endure.adapter = mAdapter

        mAdapter.attempts = db.readHistory()

        //DAY COUNTER
        val c =  Calendar.getInstance();
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var strToday = "$year-$month-$day"
        var fresh = false
        var currentAttempt = Attempt()
        var days = 0
        var dtCurrStart : LocalDate


        val textView = view!!.findViewById<TextView>(R.id.textView)
        val textLast = view!!.findViewById<TextView>(R.id.textView_last)

        if(db.checkExist()){
            //textLast.setText("Streak ongoing")
            currentAttempt = db.readData()
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            dtCurrStart = LocalDate.parse(currentAttempt.start, formatter)
            days = abs(((dtCurrStart.year - year) * 365) - ((dtCurrStart.month.value - month) * 30) + (dtCurrStart.dayOfMonth - day))
            textLast.setText("${currentAttempt.start}")
            textView.setText("$days days clean")
            fresh = false;
        }
        else{
            textLast.setText("No progress found.")
            fresh = true
        }



        chooseButton.setOnClickListener {
            val chosenDate = DatePickerDialog(this.context!!, DatePickerDialog.OnDateSetListener{view, choiceYear:Int, choiceMonth:Int, choiceDay:Int ->

                if(!fresh){
                    //var dtCurrStart = LocalDate.parse(currentAttempt.start)
                    //var days = abs(((dtCurrStart.year - year) * 365) - ((dtCurrStart.monthValue - month) * 30) + (dtCurrStart.dayOfMonth - day))
                    currentAttempt.end = strToday
                    currentAttempt.days = days
                    db.updateData(currentAttempt.id, currentAttempt)
                }

                //var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                //var dtStart = LocalDate.parse("$choiceYear-$choiceMonth-$choiceDay",formatter)
                var dec = DecimalFormat("00")
                db.insertData(Attempt("$choiceYear-${dec.format(choiceMonth)}-${dec.format(choiceDay)}"))

                if(db.checkExist()){
                    //textLast.setText("Streak ongoing")
                    currentAttempt = db.readData()

                    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    dtCurrStart = LocalDate.parse(currentAttempt.start, formatter)
                  //  var cur = LocalDate.parse("$year-$month-$day",formatter)
                   // dtCurrStart = DateTime(currentAttempt.start, formatter)

                    /*
                    println("THE YEAR IS ${dtCurrStart.year} - $year")
                    println("THE MONTH IS ${dtCurrStart.month.value} - $month")
                    println("THE DAY IS ${dtCurrStart.dayOfMonth} - $day")
*/
                    days = abs(abs((dtCurrStart.year - year) * 365) - abs((dtCurrStart.month.value - month) * 30) + abs(dtCurrStart.dayOfMonth - day))
                    //days = Days.daysBetween(dtCurrStart, cur).getDays()
                    //days = Days.daysBetween(cur, cur)
                    textLast.setText("${currentAttempt.start}")
                    textView.setText("$days days clean")
                    fresh = false;

                    mAdapter.attempts = db.readHistory()
                    mAdapter.notifyDataSetChanged()


                }


            }, year, month, day)

            chosenDate.show()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EndureFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EndureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}