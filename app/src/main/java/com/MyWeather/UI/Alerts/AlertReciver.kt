package com.MyWeather.UI.Alerts

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationCompat
import com.MyWeather.UI.Weather.MainActivity
import com.MyWeather.myweather.R


class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //Recheck
        var description = intent.getStringExtra("description")
        var sound = intent.getBooleanExtra("alarm_type", true)
        var alarmsound = if (sound) Settings.System.DEFAULT_ALARM_ALERT_URI else Settings.System.DEFAULT_NOTIFICATION_URI
        val mediaPlayer =
                MediaPlayer.create(context, alarmsound)
        mediaPlayer.start()


        val intent1 = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent1, 0)
        val notificationHelper =
                NotificationHelper(context)
        val nb: NotificationCompat.Builder =
                notificationHelper.channelNotification
                        .setContentTitle(context.getString(R.string.alerts))
                        .setContentText(context.getString(R.string.nodangerous))
                        .setContentIntent(pendingIntent)
        notificationHelper.manager?.notify(1, nb.build())
        //  }
    }
}