package com.quanticheart.sendcustonaction.monitor.receivers.verification

import android.app.ActivityManager
import android.content.Context

//
// Created by Jonn Alves on 18/10/22.
//
internal fun Context.isAppForeground(applicationPackageName: String? = null): Boolean {
    var foreground = false
    val appProcesses =
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            foreground = true
        }
    }
    return foreground
}