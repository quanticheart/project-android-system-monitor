package com.quanticheart.monitor.system.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(AlarmConstants.custonKey)) {
            context.sendBroadcast(Intent(AlarmConstants.custonKey).apply {
                val data = intent.getStringExtra(AlarmConstants.custonKey)
                putExtra(AlarmConstants.dataKey, data)
            })
        } else {
            if (intent.hasExtra(AlarmConstants.minute)) {
                context.sendBroadcast(Intent(AlarmConstants.minute))
            }
            if (intent.hasExtra(AlarmConstants.hour)) {
                context.sendBroadcast(Intent(AlarmConstants.hour))
            }
            if (intent.hasExtra(AlarmConstants.day)) {
                context.sendBroadcast(Intent(AlarmConstants.day))
            }
        }
    }
}