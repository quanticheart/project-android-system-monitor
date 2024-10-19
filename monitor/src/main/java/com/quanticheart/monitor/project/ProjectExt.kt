package com.quanticheart.monitor.project

import android.annotation.SuppressLint
import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.quanticheart.monitor.extentions.telephonyManager
import com.quanticheart.monitor.extentions.toJson
import com.quanticheart.monitor.project.model.MobileDetails
import com.quanticheart.monitor.project.model.SimpleMobileDetails
import com.quanticheart.monitor.project.system.*
import com.quanticheart.monitor.sharedPreferences.sendList
import com.quanticheart.monitor.system.app.getPackageInfo
import com.quanticheart.monitor.system.app.isAppForeground
import com.quanticheart.monitor.system.audio.getAudioDetails
import com.quanticheart.monitor.system.band.getBandDetails
import com.quanticheart.monitor.system.battery.getBatteryDetails
import com.quanticheart.monitor.system.bluetooth.getBluetoothDetails
import com.quanticheart.monitor.system.build.getBuildDetails
import com.quanticheart.monitor.system.camera.getCameraDetails
import com.quanticheart.monitor.system.cpu.getCpuDetails
import com.quanticheart.monitor.system.debug.getDebugDetails
import com.quanticheart.monitor.system.emulator.getEmulatorDetails
import com.quanticheart.monitor.system.hook.getXposedHookDetails
import com.quanticheart.monitor.system.memory.getMemoryDetails
import com.quanticheart.monitor.system.moreopen.getCheckVirtualDetails
import com.quanticheart.monitor.system.network.getNetworkDetails
import com.quanticheart.monitor.system.root.getMobileRootDetails
import com.quanticheart.monitor.system.sdcard.sdCard
import com.quanticheart.monitor.system.setting.getSettingsDetails
import com.quanticheart.monitor.system.signal.getNetRssiDetails
import com.quanticheart.monitor.system.simcard.getSimDetails
import com.quanticheart.monitor.system.uniqueid.uniqueID
import com.quanticheart.monitor.system.useragent.defaultUserAgent
import com.quanticheart.monitor.system.wifi.getWifiDetails


@SuppressLint("MissingPermission")
fun Context.getInfo() {
    val policy = StrictMode.ThreadPolicy.Builder().permitNetwork().build()
    StrictMode.setThreadPolicy(policy)

    val info = MobileDetails()
    info.details = getWifiDetails()
    info.network = getNetworkDetails()
    info.net = getNetRssiDetails()
    info.band = getBandDetails()
    info.sim = getSimDetails()

    info.pkg = getPackageInfo()
//    info.appsList = getAppsListDetails()
    info.audio = getAudioDetails()
    info.battery = getBatteryDetails()
    info.bluetooth = getBluetoothDetails()
    info.build = getBuildDetails()
    info.camera = getCameraDetails()
    info.cpu = getCpuDetails()
    info.debug = getDebugDetails()
    info.emulator = getEmulatorDetails()
    info.hookExposed = getXposedHookDetails()
    info.local = com.quanticheart.monitor.system.local.localDetails
    info.memory = getMemoryDetails()
    info.moreOpen = getCheckVirtualDetails()
    info.root = getMobileRootDetails()
    info.sdCard = sdCard
    info.settings = getSettingsDetails()
    info.uniqueID = uniqueID
    info.userAgent = defaultUserAgent()

    val manager = telephonyManager

    //For 3G check
    Log.e("TEST", manager.networkOperatorName ?: "NULL")

    val json = info.toJson()
//    Log.e("JOSN", deviceId.toString())
    Log.e("JOSN", json)
}

fun Context.getSimpleDetails(): SimpleMobileDetails {
    /**
     * Info
     */
    val info = SimpleMobileDetails()
    info.foregraound = isAppForeground()
    info.date = now()
    info.hardware = getHardwareInfo()
    info.app = getAppInfo()
    info.screen = getScreenStatus()
    info.battery = getBatteryInfo()
    info.location = getLocationInfo()
    info.connection = getNetworkInfo()

    return info
}

fun Context.sendDataCollected() {
    sendList()
}
