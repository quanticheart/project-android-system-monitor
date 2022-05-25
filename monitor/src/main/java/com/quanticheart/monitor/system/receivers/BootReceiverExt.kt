package com.quanticheart.core.system.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

fun Context.registerBootReceiver(callback: () -> Unit) {
    val b = SmartphoneBootReceive(callback)
    val filter = IntentFilter()
    filter.addAction(BootConstants.smartphoneBoot)
    registerReceiver(b, filter)
}

internal class SmartphoneBootReceive(private val callback: () -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BootConstants.smartphoneBoot -> callback()
        }
    }
}