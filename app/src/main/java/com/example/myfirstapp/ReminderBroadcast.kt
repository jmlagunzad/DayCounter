package com.example.myfirstapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ReminderBroadcast : BroadcastReceiver() {

    private val CHANNEL_ID = "endure_channel_id"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val bitmap = BitmapFactory.decodeResource(context.applicationContext.resources, R.mipmap.joker_crit_foreground)
        val bitmapSmall = BitmapFactory.decodeResource(context.applicationContext.resources, R.mipmap.morgana_icon_foreground)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.im_icon_foreground)
            .setLargeIcon(bitmapSmall)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentTitle("Looking cool, Joker!")
            .setContentText("You've been clean for ${computeDays(intent)} day/s!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager.notify(102,builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun computeDays(intent: Intent): Int{
        val c =  Calendar.getInstance();
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var currentAttempt = intent.getStringExtra("currentAttempt")
        println("on receive currentAttempt: $currentAttempt")
        var dtCurrStart = LocalDate.parse(currentAttempt, formatter)

        var days =
            Math.abs(((dtCurrStart.year - year) * 365) - ((dtCurrStart.month.value - month) * 30) + (dtCurrStart.dayOfMonth - day))

        return days
    }
}