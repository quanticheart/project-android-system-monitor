package com.quanticheart.monitor.project.system

import android.os.Build
import android.util.Log
import com.quanticheart.monitor.project.model.SimpleMobileDetails.Hardware

internal fun getHardwareInfo(): Hardware {
    val buildBean = Hardware()
    try {
        buildBean.brand = Build.BRAND
        buildBean.device = Build.DEVICE
        buildBean.manufacturer = Build.MANUFACTURER
        buildBean.model = Build.MODEL
        buildBean.releaseVersion = Build.VERSION.RELEASE
        buildBean.incremental = Build.VERSION.INCREMENTAL
        buildBean.sdkInt = Build.VERSION.SDK_INT
    } catch (e: Exception) {
        Log.e("getHardwareInfo", e.toString())
    }
    return buildBean
}