package com.example.insulin_dose_app

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.insulin_dose_app.R
import java.text.SimpleDateFormat
import java.util.*

class MyNotificationService : IntentService("MyNotificationService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val timeText = intent.getStringExtra("time")

            if (timeText != null) {
                showNotification(timeText)
            }
        }
    }

    private fun showNotification(timeText: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Vérifiez si le canal de notification doit être créé lors de l'utilisation d'Android version Oreo ou plus récente
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

        // Formatage de l'heure dans le style souhaité
        val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

        val notificationTitle = "تذكير بجرعة الانسولين"
        val notificationContent = "حان وقت جرعة الانسولين"
        val notificationDetails = "الوقت: $formattedTime"

        // Créez une intention pour lancer votre activité
        val resultIntent = Intent(this, InsulinActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, "channelID")
            .setSmallIcon(R.drawable.alert)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$notificationContent\n$notificationDetails"))
            .setSound(soundUri) // Ajouter la sonnerie ici
            .setContentIntent(pendingIntent) // Ajouter le PendingIntent ici

        notificationManager.notify(1, builder.build())
    }
}
