package com.example.myfirstapp

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
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
        val mainIntent = Intent(context, SplashScreenActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context,0,mainIntent, 0)

        val bitmap = BitmapFactory.decodeResource(context.applicationContext.resources, R.mipmap.joker_crit_foreground)
        val bitmapSmall = BitmapFactory.decodeResource(context.applicationContext.resources, R.mipmap.morgana_icon_foreground)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.im_icon_foreground)
            .setLargeIcon(bitmapSmall)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentTitle("Looking cool, Joker!")
            .setContentText("You've been clean for ${computeDays(intent)} day/s!")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager.notify(102,builder.build())

        Toast.makeText(context, "Alarm bot set to ${computeDays(intent)} days", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun computeDays(intent: Intent): Int{
        val c =  Calendar.getInstance();
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)

        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var currentAttempt = intent.getStringExtra("currentAttempt")
        var dtCurrStart = LocalDate.parse(currentAttempt, formatter)

        return Math.abs(((dtCurrStart.year - year) * 365) + ((dtCurrStart.month.value - month) * 30) + (dtCurrStart.dayOfMonth - day))
    }
}