package com.example.cashincontrol.domain.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cashincontrol.R

fun showNotification(context: Context, notificationId: Int, title: String, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("PUSH","Нет разрешения на отправку уведомлений")
            return
        }
    }

    val channelId = "local_notifications_channel"
    val name = "Local Notifications"
    val descriptionText = "Notifications for local events"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, name, importance).apply {
        description = descriptionText
    }
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)

    val largeIconBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.push_icon)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setLargeIcon(largeIconBitmap)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

