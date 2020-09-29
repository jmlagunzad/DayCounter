package com.example.myfirstapp

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
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

    private val CHANNEL_ID = "endure_channel_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        createNotificationChannel()

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply{
                description = descriptionText
            }
            val notificationManager: NotificationManager = activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(days: Int = 0){
        val bitmap = BitmapFactory.decodeResource(this.activity!!.applicationContext.resources, R.mipmap.joker_crit_foreground)
        val bitmapSmall = BitmapFactory.decodeResource(this.activity!!.applicationContext.resources, R.mipmap.morgana_icon_foreground)

        val builder = NotificationCompat.Builder(this.context!!, CHANNEL_ID)
            .setSmallIcon(R.mipmap.im_icon_foreground)
            .setLargeIcon(bitmapSmall)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentTitle("Looking cool, Joker!")
            .setContentText("You've been clean for $days day/s!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this.context!!)){
            notify(101,builder.build())
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

        sendNotification(days)

        chooseButton.setOnClickListener {
            val chosenDate = DatePickerDialog(this.context!!, DatePickerDialog.OnDateSetListener{view, choiceYear:Int, choiceMonth:Int, choiceDay:Int ->

                if(!fresh){
                    currentAttempt.end = strToday
                    currentAttempt.days = days
                    db.updateData(currentAttempt.id, currentAttempt)
                }

                var dec = DecimalFormat("00")
                db.insertData(Attempt("$choiceYear-${dec.format(choiceMonth)}-${dec.format(choiceDay)}"))

                if(db.checkExist()){
                    currentAttempt = db.readData()

                    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    dtCurrStart = LocalDate.parse(currentAttempt.start, formatter)

                    days = abs(abs((dtCurrStart.year - year) * 365) - abs((dtCurrStart.month.value - month) * 30) + abs(dtCurrStart.dayOfMonth - day))

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