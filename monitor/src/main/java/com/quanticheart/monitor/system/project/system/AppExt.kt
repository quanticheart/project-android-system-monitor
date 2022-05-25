package com.quanticheart.monitor.system.project.system

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.quanticheart.monitor.system.project.model.SimpleMobileDetails
import com.quanticheart.monitor.system.system.others.getSign

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
