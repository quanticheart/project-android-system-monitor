package com.quanticheart.monitor.system.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

fun Context.configAlarmActions(callback: AlarmActionListeners) {
    val b = AppReceive(callback)
    val filter = IntentFilter()
    filter.addAction(AlarmConstants.minute)
    filter.addAction(AlarmConstants.hour)
    filter.addAction(AlarmConstants.day)
    filter.addAction(AlarmConstants.custonKey)
    registerReceiver(b, filter)

    ProjectAlarms.startAllAlarms(this)
}

class AppReceive(private val callback: AlarmActionListeners) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmConstants.minute -> callback.receiveMinuteAction()
            AlarmConstants.hour -> callback.receiveHourAction()
            AlarmConstants.day -> callback.receiveDayAction()
            AlarmConstants.custonKey -> intent.getStringExtra(AlarmConstants.dataKey)?.let {
                    callback.customAction(it)
                }
        }
    }
}