package com.example.cashincontrol.domain.notifications

import android.content.Context
import androidx.core.app.NotificationCompat

class Notification {
    companion object {
        const val CHANNEL_ID = "1"

        fun setupNotification(context: Context) {
            val nBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Nice")
                .setContentText("Nicew")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
        }
    }
}