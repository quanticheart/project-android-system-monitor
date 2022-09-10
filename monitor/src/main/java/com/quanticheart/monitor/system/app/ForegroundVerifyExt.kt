package com.quanticheart.monitor.system.app

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import com.quanticheart.monitor.extentions.log

//
// Created by Jonn Alves on 09/09/22.
//
fun Context.isAppForeground(): Boolean {
    var foreground = false
    val appProcesses =
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            log("Foreground App", appProcess.processName)
            foreground = true
        } else {
            log("Foreground App", "Background = " + appProcess.processName)
        }
    }
    return foreground
}