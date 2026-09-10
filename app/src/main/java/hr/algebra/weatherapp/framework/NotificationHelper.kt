package hr.algebra.weatherapp.framework

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import hr.algebra.weatherapp.HostActivity
import hr.algebra.weatherapp.R

private const val CHANNEL_ID = "weather_channel"
private const val CHANNEL_NAME = "Weather notifications"
private const val NOTIFICATION_ID = 1

fun Context.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}

fun Context.sendWeatherNotification(cityName: String) {
    android.util.Log.d("NOTIF_TEST", "sendWeatherNotification pozvan za: $cityName")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            android.util.Log.d("NOTIF_TEST", "PERMISSION NIJE DOPUŠTEN, izlazim")
            return
        }
    }
    android.util.Log.d("NOTIF_TEST", "Permission OK/nije potreban, saljem notifikaciju")

    val intent = Intent(this, HostActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(
        this, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.weather)
        .setContentTitle("Grad dodan")
        .setContentText("$cityName je uspješno dodan na tvoju listu lokacija")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    val manager = getSystemService(NotificationManager::class.java)
    manager.notify(NOTIFICATION_ID, notification)

    android.util.Log.d("NOTIF_TEST", "notify() pozvan")
}