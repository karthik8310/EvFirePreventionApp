package com.ev.fireprevention.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ev.fireprevention.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "ev_fire_prevention_alerts"
        const val CHANNEL_NAME = "EV Fire Prevention Alerts"
        const val CHANNEL_DESCRIPTION = "Notifications for battery health alerts"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String, notificationId: Int) {
        // Check for permission before showing (handled in UI/ViewModel usually, but good to check here too)
        // For simplicity in this helper, we assume permission is granted or we catch the security exception
        try {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert) // Use a default icon for now
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                // notify() requires the POST_NOTIFICATIONS permission on Android 13+
                // We suppress the lint warning here because we'll request it in the UI
                try {
                    notify(notificationId, builder.build())
                } catch (e: SecurityException) {
                    // Permission not granted
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
