package com.example.myfirstapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val c =  Calendar.getInstance();
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        chooseButton.setOnClickListener {
            val chosenDate = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, choiceYear:Int, choiceMonth:Int, choiceDay:Int ->
            editTextDate.setText("" + choiceYear + choiceMonth + choiceDay)
            }, year, month, day)

            chosenDate.show()
        }

    }
}