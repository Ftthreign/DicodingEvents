package com.ftthreign.dicodingevents.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ftthreign.dicodingevents.R
import com.ftthreign.dicodingevents.data.remote.EventRepository
import com.ftthreign.dicodingevents.di.Injection
import java.text.SimpleDateFormat
import java.util.Locale

class MyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val eventRepository: EventRepository = Injection.provideRepository(context)

    override suspend fun doWork(): Result {
        return try {
            val nearestEvent = eventRepository.getNearestEvent()
            val event = nearestEvent?.listEvents?.firstOrNull()
            if (event != null) {
                val formattedDate = formatEventDate(event.beginTime)
                val notificationTitle = "Event Reminder: $formattedDate"
                showNotification(notificationTitle, event.name, event.link)
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun formatEventDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(dateString)

        val outputFormat = SimpleDateFormat("d MMM, HH:mm", Locale.getDefault())
        return date?.let { outputFormat.format(it) } ?: "Unknown date"
    }

    private fun showNotification(title: String, description: String, link: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        createNotificationChannel()

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_calendar_month_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(description))
            .setAutoCancel(true)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for event reminders"
            }

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "event_reminder_channel"
        const val CHANNEL_NAME = "Event Reminder Channel"
    }
}
