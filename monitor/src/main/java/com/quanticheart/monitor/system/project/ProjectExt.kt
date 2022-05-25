package com.quanticheart.monitor.system.project

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.quanticheart.monitor.system.extentions.toJson
import com.quanticheart.monitor.system.project.model.MobileDetails
import com.quanticheart.monitor.system.project.model.SimpleMobileDetails
import com.quanticheart.monitor.system.project.system.*
import com.quanticheart.monitor.system.sharedPreferences.insertSimpleDetails
import com.quanticheart.monitor.system.sharedPreferences.sendList
import com.quanticheart.monitor.system.system.app.getPackageInfo
import com.quanticheart.monitor.system.system.audio.getAudioDetails
import com.quanticheart.monitor.system.system.band.getBandDetails
import com.quanticheart.monitor.system.system.battery.getBatteryDetails
import com.quanticheart.monitor.system.system.bluetooth.getBluetoothDetails
import com.quanticheart.monitor.system.system.build.getBuildDetails
import com.quanticheart.monitor.system.system.camera.getCameraDetails
import com.quanticheart.monitor.system.system.cpu.getCpuDetails
import com.quanticheart.monitor.system.system.debug.getDebugDetails
import com.quanticheart.monitor.system.system.emulator.getEmulatorDetails
import com.quanticheart.monitor.system.system.hook.getXposedHookDetails
import com.quanticheart.monitor.system.system.memory.getMemoryDetails
import com.quanticheart.monitor.system.system.moreopen.getCheckVirtualDetails
import com.quanticheart.monitor.system.system.network.getNetworkDetails
import com.quanticheart.monitor.system.system.root.getMobileRootDetails
import com.quanticheart.monitor.system.system.sdcard.sdCard
import com.quanticheart.monitor.system.system.setting.getSettingsDetails
import com.quanticheart.monitor.system.system.signal.getNetRssiDetails
import com.quanticheart.monitor.system.system.simcard.getSimDetails
import com.quanticheart.monitor.system.system.uniqueid.uniqueID
import com.quanticheart.monitor.system.system.useragent.defaultUserAgent
import com.quanticheart.monitor.system.system.wifi.getWifiDetails

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
    info.local = com.quanticheart.monitor.system.system.local.localDetails
    info.memory = getMemoryDetails()
    info.moreOpen = getCheckVirtualDetails()
    info.root = getMobileRootDetails()
    info.sdCard = sdCard
    info.settings = getSettingsDetails()
    info.uniqueID = uniqueID
    info.userAgent = defaultUserAgent()

    val json = info.toJson()
    Log.e("JOSN", json)
//    val window = getMobScreen(window)
}

fun Context.getSimpleDetails(): SimpleMobileDetails {
    /**
     * Info
     */
    val info = SimpleMobileDetails()
    info.id = deviceUUID
    info.id2 = deviceUUID2
    info.id3 = deviceUUID3
    info.id4 = uuid
    info.date = now()
    info.hardware = getHardwareInfo()
    info.app = getAppInfo()
    info.screen = getScreenStatus()
    info.battery = getBatteryInfo()
    info.location = getLocationInfo()
    info.connection = getNetworkInfo()

    return info
}

fun Context.collectData() {
    insertSimpleDetails(getSimpleDetails())
}

fun Context.sendDataCollected() {
    sendList()
}
