package com.example.myfirstapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcast : BroadcastReceiver() {

    private val CHANNEL_ID = "endure_channel_id"

    override fun onReceive(context: Context, intent: Intent) {
        val bitmap = BitmapFactory.decodeResource(context.applicationContext.resources, R.mipmap.joker_crit_foreground)
        val bitmapSmall = BitmapFactory.decodeResource(context.applicationContext.resources, R.mipmap.morgana_icon_foreground)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.im_icon_foreground)
            .setLargeIcon(bitmapSmall)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentTitle("Looking cool, Joker!")
            .setContentText("You've been clean for ___ day/s!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager.notify(102,builder.build())
    }
}