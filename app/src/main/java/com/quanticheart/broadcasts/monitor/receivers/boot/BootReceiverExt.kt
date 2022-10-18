package com.quanticheart.sendcustonaction.monitor.receivers.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.quanticheart.sendcustonaction.monitor.alarm.AppAlarm

fun Context.registerMonitor() {
    val b = SmartphoneBootReceive {
        AppAlarm.start(this)
    }
    val filter = IntentFilter()
    filter.addAction(BootConstants.smartphoneBoot)
    registerReceiver(b, filter)
    AppAlarm.start(this)
}

internal class SmartphoneBootReceive(private val callback: () -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BootConstants.smartphoneBoot -> callback()
        }
    }
}