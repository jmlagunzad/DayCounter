package com.example.myfirstapp

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
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

    /*
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
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendAlert(currentAttempt: String){
        var intent = Intent(this.activity!!,ReminderBroadcast::class.java)
        intent.putExtra("currentAttempt", currentAttempt)
        var pendingIntent = PendingIntent.getBroadcast(this.activity!!,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        var alarmManager : AlarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var timeAtButtonClick: Long = System.currentTimeMillis()

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeAtButtonClick,
            AlarmManager.INTERVAL_HALF_DAY
            //1000 * 30
            , pendingIntent)
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
        var fresh = false
        var currentAttempt = Attempt()

        val textView = view!!.findViewById<TextView>(R.id.textView)
        val textLast = view!!.findViewById<TextView>(R.id.textView_last)

        if(db.checkExist()){
            currentAttempt = db.readData()
            textLast.setText("Started on ${currentAttempt.start}")
            textView.setText("${computeDays(currentAttempt.start)} days clean")
            fresh = false;
        }
        else{
            textLast.setText("No progress found.")
            fresh = true
        }

        chooseButton.setOnClickListener {
            val chosenDate = DatePickerDialog(this.context!!, DatePickerDialog.OnDateSetListener{view, choiceYear:Int, choiceMonth:Int, choiceDay:Int ->

                if(!fresh){
                    currentAttempt.end = "$year-$month-$day"
                    currentAttempt.days = computeDays(currentAttempt.start)
                    db.updateData(currentAttempt.id, currentAttempt)
                }

                var dec = DecimalFormat("00")
                db.insertData(Attempt("$choiceYear-${dec.format(choiceMonth)}-${dec.format(choiceDay)}"))


                var start = ""

                if(db.checkExist()){
                    currentAttempt = db.readData()
                    textLast.setText("Started on ${currentAttempt.start}")
                    textView.setText("${computeDays(currentAttempt.start)} days clean")
                    fresh = false;

                    mAdapter.attempts = db.readHistory()
                    mAdapter.notifyDataSetChanged()

                    Log.d("StartDate", currentAttempt.start)
                    start = currentAttempt.start


                }

                sendAlert(start)

            }, year, month, day)

            chosenDate.show()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun computeDays(currentAttempt: String): Int{
        val c =  Calendar.getInstance();
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var dtCurrStart = LocalDate.parse(currentAttempt, formatter)

//        println("Math.abs(((${dtCurrStart.year} - $year) * 365) - ((${dtCurrStart.month.value} - $month) * 30) + (${dtCurrStart.dayOfMonth} - $day))")
//        var daysElapsed = Math.abs(((dtCurrStart.year - year) * 365) + ((dtCurrStart.month.value - month) * 30) + (dtCurrStart.dayOfMonth - day))
//        println("dtCurrStart: $dtCurrStart -- $year-$month-$day")
//        println("days = $daysElapsed")


        return Math.abs(((dtCurrStart.year - year) * 365) + ((dtCurrStart.month.value - month) * 30) + (dtCurrStart.dayOfMonth - day))
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