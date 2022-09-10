package com.quanticheart.monitor.project.system

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.quanticheart.monitor.extentions.log
import com.quanticheart.monitor.project.model.SimpleMobileDetails

internal fun Context.getAppInfo(): SimpleMobileDetails.App {
    val packageBean = SimpleMobileDetails.App()
    try {
        val packageManager = packageManager
        val applicationInfo = applicationInfo
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val packageName = packageInfo.packageName

        packageBean.appName = packageManager.getApplicationLabel(applicationInfo).toString()
        packageBean.packageName = packageName

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageBean.appVersionCode = packageInfo.longVersionCode
        } else {
            packageBean.appVersionCode = packageInfo.versionCode.toLong()
        }

        packageBean.appVersionName = packageInfo.versionName
        packageBean.targetSdkVersion = applicationInfo.targetSdkVersion

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            packageBean.minSdkVersion = applicationInfo.minSdkVersion
        }

        packageBean.lastUpdateTime = packageInfo.lastUpdateTime.toDate()
        packageBean.firstInstallTime = packageInfo.firstInstallTime.toDate()
    } catch (e: Exception) {
        Log.e("getAppDetails", e.toString())
    }
    return packageBean
}

fun Context.isAppForeground(): Boolean {
    var foreground = false
    val appProcesses =
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            log("Foreground App", appProcess.processName)
            foreground = true
        } else {
            log("Foreground App", "Background = " + appProcess.processName)
        }
    }
    return foreground
}
