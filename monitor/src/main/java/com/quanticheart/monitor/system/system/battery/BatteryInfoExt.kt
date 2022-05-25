package com.quanticheart.monitor.system.system.battery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.quanticheart.monitor.system.extentions.UNKNOWN_PARAM
import com.quanticheart.monitor.system.system.others.DoubleUtil

private val TAG = "Battery"

fun Context.getBatteryDetails(): Battery {
    val batteryBean = Battery()
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
            // unknown=1, good=2, overheat=3, dead=4, over voltage=5, unspecified failure=6, cold=7
            val health: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
            val present: Boolean =
                batteryStatus.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
            val technology: String? =
                batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            val temperature: Int =
                batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
            val voltage: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
            batteryBean.br = DoubleUtil.mul(batteryLevel, 100.0).toString() + "%"
            batteryBean.status = batteryStatus(status)
            batteryBean.plugState = batteryPlugged(plugState)
            batteryBean.health = batteryHealth(health)
            batteryBean.isPresent = present
            batteryBean.technology = technology
            batteryBean.temperature = (temperature / 10).toString() + "℃"
            if (voltage > 1000) {
                batteryBean.voltage = (voltage / 1000f).toString() + "V"
            } else {
                batteryBean.voltage = voltage.toString() + "V"
            }
            batteryBean.power = getBatteryCapacity(this)
        }
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return batteryBean
}

@SuppressLint("PrivateApi")
private fun getBatteryCapacity(context: Context): String {
    val mPowerProfile: Any
    var batteryCapacity = 0.0
    val powerProfileClass = "com.android.internal.os.PowerProfile"
    try {
        mPowerProfile = Class.forName(powerProfileClass)
            .getConstructor(Context::class.java)
            .newInstance(context)
        batteryCapacity = Class.forName(powerProfileClass)
            .getMethod("getBatteryCapacity")
            .invoke(mPowerProfile) as Double
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return batteryCapacity.toString() + "mAh"
}

private fun batteryHealth(health: Int): String {
    var healthBat: String = UNKNOWN_PARAM
    when (health) {
        BatteryManager.BATTERY_HEALTH_COLD -> healthBat = "cold"
        BatteryManager.BATTERY_HEALTH_DEAD -> healthBat = "dead"
        BatteryManager.BATTERY_HEALTH_GOOD -> healthBat = "good"
        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> healthBat = "overVoltage"
        BatteryManager.BATTERY_HEALTH_OVERHEAT -> healthBat = "overheat"
        BatteryManager.BATTERY_HEALTH_UNKNOWN -> healthBat = "unknown"
        BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> healthBat = "unspecified"
        else -> {}
    }
    return healthBat
}

private fun batteryStatus(status: Int): String {
    var healthBat: String = UNKNOWN_PARAM
    when (status) {
        BatteryManager.BATTERY_STATUS_CHARGING -> healthBat = "charging"
        BatteryManager.BATTERY_STATUS_DISCHARGING -> healthBat = "disCharging"
        BatteryManager.BATTERY_STATUS_FULL -> healthBat = "full"
        BatteryManager.BATTERY_STATUS_NOT_CHARGING -> healthBat = "notCharging"
        BatteryManager.BATTERY_STATUS_UNKNOWN -> healthBat = "unknown"
        else -> {}
    }
    return healthBat
}

private fun batteryPlugged(status: Int): String {
    var healthBat: String = UNKNOWN_PARAM
    when (status) {
        BatteryManager.BATTERY_PLUGGED_AC -> healthBat = "ac"
        BatteryManager.BATTERY_PLUGGED_USB -> healthBat = "usb"
        BatteryManager.BATTERY_PLUGGED_WIRELESS -> healthBat = "wireless"
        else -> {}
    }
    return healthBat
}