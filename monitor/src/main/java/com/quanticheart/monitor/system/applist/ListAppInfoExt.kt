package com.quanticheart.monitor.system.applist

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.quanticheart.monitor.system.others.getSign

internal fun Context.getAppsListDetails(): List<ListAppBean> {
    val appLists: MutableList<ListAppBean> = ArrayList()
    try {
        val packageManager: PackageManager = packageManager
        val packageInfoList: List<PackageInfo> = packageManager.getInstalledPackages(0)
        for (packageInfo in packageInfoList) {
            val listAppBean = ListAppBean()
            listAppBean.packageName = packageInfo.packageName
            listAppBean.appVersionName = packageInfo.versionName
            listAppBean.appName =
                packageInfo.applicationInfo.loadLabel(packageManager).toString()
            listAppBean.description =
                packageInfo.applicationInfo.loadDescription(packageManager)
            listAppBean.icon = packageInfo.applicationInfo.loadIcon(packageManager)
            listAppBean.targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion
            listAppBean.lastUpdateTime = packageInfo.lastUpdateTime
            listAppBean.firstInstallTime = packageInfo.firstInstallTime
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listAppBean.minSdkVersion = packageInfo.applicationInfo.minSdkVersion
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                listAppBean.appVersionCode = packageInfo.longVersionCode
            } else {
                listAppBean.appVersionCode = packageInfo.versionCode.toLong()
            }
            listAppBean.packageSign = getSign(this, listAppBean.packageName)
            listAppBean.isSystem =
                ApplicationInfo.FLAG_SYSTEM and packageInfo.applicationInfo.flags != 0
            appLists.add(listAppBean)
        }
    } catch (e: Exception) {
    }
    return appLists
}
