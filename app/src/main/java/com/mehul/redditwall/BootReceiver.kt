package com.mehul.redditwall

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        val setAlarm = prefs.getBoolean(SettingsActivity.RANDOM_ENABLED, false)
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && setAlarm) {
            val wallAlarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val wallChangeIntent = Intent(context, ChangeWallpaper::class.java).apply {
                action = "CHANGE_WALL"
            }
            val pending = PendingIntent.getBroadcast(context, 2, wallChangeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            val chosenTime = prefs.getInt(SettingsActivity.RANDOM_INTERVAL, 0)
            val interval = (chosenTime + 1) * 60 * 60 * 1000
            val triggerTime = SystemClock.elapsedRealtime() + interval
            Toast.makeText(context, "Random refresh enabled", Toast.LENGTH_SHORT).show()
            wallAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, interval.toLong(), pending)
        }
    }
}
