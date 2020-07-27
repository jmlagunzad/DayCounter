package com.example.myfirstapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val c =  Calendar.getInstance();
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        textView.text = "Today is $month-$day-$year"



        chooseButton.setOnClickListener {
            val chosenDate = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, choiceYear:Int, choiceMonth:Int, choiceDay:Int ->

                var daysDiff = abs(((choiceYear - year) * 365) - ((choiceMonth - month) * 30) + (choiceDay - day))

                textView.setText("It's been $daysDiff day/s since you've been sober. Good job!")
            }, year, month, day)

            chosenDate.show()
        }

    }
}