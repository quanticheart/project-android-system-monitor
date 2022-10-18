package com.quanticheart.sendcustonaction.monitor.receivers.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.quanticheart.sendcustonaction.monitor.alarm.AppAlarm

@Suppress("PrivatePropertyName", "SpellCheckingInspection")
internal class BootReceiver : BroadcastReceiver() {
    private val BOOT = Intent.ACTION_BOOT_COMPLETED
    private val BOOT_COMPLETED = "android.intent.action.ACTION_BOOT_COMPLETED"
    private val SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN"
    private val POWERON = "android.intent.action.QUICKBOOT_POWERON"
    private val POWERON_HTC = "com.htc.intent.action.QUICKBOOT_POWERON"
    private val POWERON_MIUI = "android.intent.action.REBOOT"

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BOOT, BOOT_COMPLETED, SHUTDOWN, POWERON, POWERON_HTC, POWERON_MIUI -> {
                AppAlarm.start(context)
            }
        }
    }
}
