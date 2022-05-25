package com.quanticheart.monitor.system.system.build

import android.os.Build
import android.util.Log

private val TAG = "Build"

internal fun getBuildDetails(): com.quanticheart.monitor.system.system.build.Build {
    val buildBean = com.quanticheart.monitor.system.system.build.Build()
    try {
        buildBean.board = Build.BOARD
        buildBean.bootloader = Build.BOOTLOADER
        buildBean.brand = Build.BRAND
        buildBean.device = Build.DEVICE
        buildBean.display = Build.DISPLAY
        buildBean.fingerprint = Build.FINGERPRINT
        buildBean.hardware = Build.HARDWARE
        buildBean.host = Build.HOST
        buildBean.id = Build.ID
        buildBean.manufacturer = Build.MANUFACTURER
        buildBean.model = Build.MODEL
        buildBean.product = Build.PRODUCT
        buildBean.radio = Build.getRadioVersion()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                buildBean.serial = Build.getSerial()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        } else {
            buildBean.serial = Build.SERIAL
        }
        buildBean.tags = Build.TAGS
        buildBean.time = Build.TIME
        buildBean.type = Build.TYPE
        buildBean.user = Build.USER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            buildBean.osVersion = Build.VERSION.BASE_OS
            buildBean.previewSdkInt = Build.VERSION.PREVIEW_SDK_INT
            buildBean.securityPatch = Build.VERSION.SECURITY_PATCH
        }
        buildBean.releaseVersion = Build.VERSION.RELEASE
        buildBean.incremental = Build.VERSION.INCREMENTAL
        buildBean.sdkInt = Build.VERSION.SDK_INT
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return buildBean
}
