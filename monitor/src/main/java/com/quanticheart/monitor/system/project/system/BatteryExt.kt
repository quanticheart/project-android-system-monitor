package com.quanticheart.monitor.system.project.system

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.quanticheart.monitor.system.extentions.UNKNOWN_PARAM
import com.quanticheart.monitor.system.project.model.SimpleMobileDetails
import com.quanticheart.monitor.system.system.others.DoubleUtil

private const val tag = "getBatteryDetails"

fun Context.getBatteryInfo(): SimpleMobileDetails.Battery {
    val batteryBean = SimpleMobileDetails.Battery()
    try {
        val batteryStatus: Intent? = registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        if (batteryStatus != null) {
            val level: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            var batteryLevel = -1.0
            if (level != -1 && scale != -1) {
                batteryLevel = DoubleUtil.divide(level.toDouble(), scale.toDouble())
            }
            // unknown=1, charging=2, discharging=3, not charging=4, full=5
            val status: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            // ac=1, usb=2, wireless=4
            val plugState: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

            batteryBean.percentage = DoubleUtil.mul(batteryLevel, 100.0)
            batteryBean.charge = batteryStatus(status)
            batteryBean.plugged = batteryPlugged(plugState)

        }
    } catch (e: Exception) {
        Log.e(tag, e.toString())
    }
    return batteryBean
}

private fun batteryStatus(status: Int) = when (status) {
    BatteryManager.BATTERY_STATUS_CHARGING -> "charging"
    BatteryManager.BATTERY_STATUS_DISCHARGING -> "disCharging"
    BatteryManager.BATTERY_STATUS_FULL -> "full"
    BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "notCharging"
    BatteryManager.BATTERY_STATUS_UNKNOWN -> "unknown"
    else -> UNKNOWN_PARAM
}

private fun batteryPlugged(status: Int) = when (status) {
    BatteryManager.BATTERY_PLUGGED_AC -> "ac"
    BatteryManager.BATTERY_PLUGGED_USB -> "usb"
    BatteryManager.BATTERY_PLUGGED_WIRELESS -> "wireless"
    else -> "not plugged"
}

