package com.quanticheart.sendcustonaction.monitor.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.quanticheart.sendcustonaction.monitor.receivers.verification.AppVerification

object AppAlarm {
    fun start(context: Context) {
        getManager(context)
    }

    fun cancel(context: Context) {
        cancelManager(context)
    }

    private var pi: PendingIntent? = null
    private var am: AlarmManager? = null

    /**
     * This alarm call in try download
     */
    private fun getManager(context: Context) {
        val mIntent = Intent(context, AppVerification::class.java)
        mIntent.putExtra("app_validation", true)
        am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pi = PendingIntent.getBroadcast(
            context,
            4,
            mIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        am?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + (1 * 60000),
            (1 * 60000).toLong(),
            pi!!
        )
    }

    private fun cancelManager(context: Context) {
        val mIntent = Intent(context, AppVerification::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 4, mIntent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager?.cancel(pendingIntent)
    }
}
