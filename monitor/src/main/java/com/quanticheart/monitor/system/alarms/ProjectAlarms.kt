package com.quanticheart.monitor.system.alarms

import android.content.Context
import com.quanticheart.monitor.system.alarms.AlarmManagerUtil.startByDay
import com.quanticheart.monitor.system.alarms.AlarmManagerUtil.startByHours
import com.quanticheart.monitor.system.alarms.AlarmManagerUtil.startByMinutes

object ProjectAlarms {
    fun startAllAlarms(context: Context) {
        startAlarmByMinutes(context)
        startAlarmByHours(context)
        startAlarmEveryDay(context)
    }

    private fun startAlarmEveryDay(context: Context) {
        startByDay(context, 18)
        startByDay(context, "otherActionDay", 12, 10)
    }

    private fun startAlarmByHours(context: Context) {
        startByHours(context, 1)
        startByHours(context, "otherActionHour", 6,11)
    }

    private fun startAlarmByMinutes(context: Context) {
        startByMinutes(context, 1)
//        startByMinutes(context, "otherActionMinute", 15,15)
    }
}