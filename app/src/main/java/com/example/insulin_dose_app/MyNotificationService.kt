package com.example.insulin_dose_app

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.insulin_dose_app.R

class MyNotificationService : IntentService("MyNotificationService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val insulinText = intent.getStringExtra("insulin")
            val dateText = intent.getStringExtra("date")
            val timeText = intent.getStringExtra("time")

            if (insulinText != null && dateText != null && timeText != null) {
                showNotification(insulinText, dateText, timeText)
            }
        }
    }

    private fun showNotification(insulinText: String, dateText: String, timeText: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the notification channel needs to be created when using Android version Oreo or newer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channelID",
                "Insulin Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Ajouter la sonnerie

         val soundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.notification_sound)

        val builder = NotificationCompat.Builder(this, "channelID")
            .setSmallIcon(R.drawable.alert)
            .setContentTitle("تذكير بجرعة الانسولين")
            .setContentText("حان وقت جرعة الانسولين\nكمية: $insulinText\nتاريخ: $dateText\nوقت: $timeText")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText("حان وقت جرعة الانسولين"))
            .setSound(soundUri) // Ajouter la sonnerie ici

        notificationManager.notify(1, builder.build())
    }
}
