package com.quanticheart.monitor.system.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

abstract class CustonReceiver : BroadcastReceiver() {

    abstract val actionKey: String

    abstract val requestCode: Int

    abstract fun type(): Pair<AlarmTypes, Int>

    abstract fun callback(intent: Intent)

    abstract fun receiver(): Class<*>

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(AlarmConstants.custonKey)) {
            callback(intent)
        }
    }

    @Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
    fun register(context: Context) {
        when (type()?.first) {
            AlarmTypes.MINUTE -> AlarmManagerUtil.startByMinutes(
                context,
                actionKey,
                type()?.second ?: 60,
                requestCode,
                receiver()
            )
            AlarmTypes.HOUR -> AlarmManagerUtil.startByHours(
                context,
                actionKey,
                type()?.second ?: 1,
                requestCode,
                receiver()
            )
            AlarmTypes.DAY -> AlarmManagerUtil.startByDay(
                context,
                actionKey,
                type()?.second ?: 18,
                requestCode,
                receiver()
            )
        }
    }

    fun cancelAlarm(context: Context) {
        AlarmManagerUtil.cancelService(
            context,
            receiver(),
            requestCode
        )
    }
}

