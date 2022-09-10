package com.quanticheart.monitor.system.app

import android.content.Context
import android.content.Intent
import android.os.Build
import com.quanticheart.monitor.system.others.getSign

internal fun Context.getPackageInfo(): Package {
    val packageBean = Package()
    try {
        val packageManager = packageManager
        val applicationInfo = applicationInfo
        packageBean.appName = packageManager.getApplicationLabel(applicationInfo).toString()
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val packageName = packageInfo.packageName
        packageBean.packageName = packageName
        packageBean.packageSign = getSign(this, packageName)
        packageBean.description = applicationInfo.loadDescription(packageManager)
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
        packageBean.launcherAppName = getLauncherPackageName(this)
        packageBean.lastUpdateTime = packageInfo.lastUpdateTime
        packageBean.firstInstallTime = packageInfo.firstInstallTime
    } catch (e: Exception) {
    }
    return packageBean
}

private fun getLauncherPackageName(context: Context): String {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    val res = context.packageManager.resolveActivity(intent, 0)
    if (res!!.activityInfo == null) {
        return "unknown"
    }
    return if (res.activityInfo.packageName == "android") {
        "unknown"
    } else {
        res.activityInfo.packageName
    }
}