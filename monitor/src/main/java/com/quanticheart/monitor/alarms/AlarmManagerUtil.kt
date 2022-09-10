package com.quanticheart.monitor.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.util.*

object AlarmManagerUtil {
    fun startByDay(
        context: Context,
        customKey: String = AlarmConstants.custonKey,
        hourToStart: Int,
        requestCode: Int,
        alarmBroadcast: Class<*>? = AlarmActionReceiver::class.java,
    ) {
        val mIntent = Intent(context, alarmBroadcast)
        mIntent.putExtra(AlarmConstants.day, true)
        if (customKey != AlarmConstants.custonKey) {
            mIntent.putExtra(AlarmConstants.custonKey, customKey)
        }
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hourToStart // For 1 PM or 2 PM
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val pi = PendingIntent.getBroadcast(
            context,
            requestCode,
            mIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pi
        )
    }

    fun startByDay(
        context: Context,
        hourToStart: Int,
        requestCode: Int = AlarmConstants.REQUEST_CODE_ALARM_DAY
    ) {
        startByDay(context, AlarmConstants.custonKey, hourToStart, requestCode)
    }

    fun startByHours(
        context: Context, hoursToStart: Int,
        requestCode: Int = AlarmConstants.REQUEST_CODE_ALARM_HOUR
    ) {
        construct(
            context,
            AlarmConstants.custonKey,
            AlarmConstants.hour,
            (hoursToStart * 60000) * 60,
            requestCode
        )
    }

    fun startByHours(
        context: Context,
        customKey: String = AlarmConstants.custonKey,
        hoursToStart: Int,
        requestCode: Int = AlarmConstants.REQUEST_CODE_ALARM_HOUR,
        alarmBroadcast: Class<*>? = AlarmActionReceiver::class.java
    ) {
        construct(
            context,
            customKey,
            AlarmConstants.hour,
            (hoursToStart * 60000) * 60,
            requestCode,
            alarmBroadcast
        )
    }

    fun startByMinutes(
        context: Context,
        minutesToStart: Int,
        requestCode: Int = AlarmConstants.REQUEST_CODE_ALARM_MINUTE
    ) {
        construct(
            context,
            AlarmConstants.custonKey,
            AlarmConstants.minute,
            (minutesToStart * 60000),
            requestCode
        )
    }

    fun startByMinutes(
        context: Context,
        customKey: String = AlarmConstants.custonKey,
        minutesToStart: Int,
        requestCode: Int = AlarmConstants.REQUEST_CODE_ALARM_MINUTE,
        alarmBroadcast: Class<*>? = AlarmActionReceiver::class.java,
        bundle: Bundle? = null
    ) {
        construct(
            context,
            customKey,
            AlarmConstants.minute,
            (minutesToStart * 60000),
            requestCode,
            alarmBroadcast,
            bundle
        )
    }

    private fun construct(
        context: Context,
        customKey: String = AlarmConstants.custonKey,
        type: String,
        startInt: Int,
        requestCode: Int,
        alarmBroadcast: Class<*>? = AlarmActionReceiver::class.java,
        bundle: Bundle? = null
    ) {
        val mIntent = Intent(context, alarmBroadcast)
        mIntent.putExtra(type, true)
        bundle?.let { mIntent.putExtras(it) }

        if (customKey != AlarmConstants.custonKey) {
            mIntent.putExtra(AlarmConstants.custonKey, customKey)
        }
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(
            context,
            requestCode,
            mIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + startInt,
            startInt.toLong(),
            pi
        )
    }

    fun cancelService(
        context: Context,
        alarmBroadcast: Class<*>,
        requestCode: Int
    ) {
        val mIntent = Intent(context, alarmBroadcast)
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager?.cancel(pendingIntent)
    }
}