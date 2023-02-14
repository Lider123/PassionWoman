package ru.babaetskv.passionwoman.app.push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.messaging.RemoteMessage
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.MainActivity
import timber.log.Timber
import kotlin.random.Random

class AppNotificationManager(
    private val context: Context
) {
    private val notificationChannelId: String
        get() = context.getString(R.string.notification_channel_id)
    private val notificationManager: NotificationManager
        get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(message: RemoteMessage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel()
        val notification = createNotification(message)
        with(notificationManager) {
            Timber.e("notify") // TODO: remove
            notify(Random.nextInt(200), notification)
        }
    }

    // TODO: parse message
    private fun createNotification(message: RemoteMessage): Notification {
        val pendingIntent = createPendingIntent(message)
        return NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.ic_push)
            .setColor(ContextCompat.getColor(context, R.color.secondary))
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    // TODO: parse message
    private fun createPendingIntent(message: RemoteMessage): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = context.getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(notificationChannelId, name, importance)
        notificationManager.createNotificationChannel(channel)
    }
}