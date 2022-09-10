package com.quanticheart.monitor.system.wifilist

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import com.quanticheart.monitor.system.wifilist.WifiBean.WifiResultBean

fun Context.getWifiList(wifiScanListener: WifiScanListener?) {
    if (wifiScanListener == null) {
        throw NullPointerException("the WifiScanListener is null")
    }
    val startTime = System.currentTimeMillis()
    val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager?
    val wifiBean = WifiBean()
    if (wifiManager == null) {
        wifiScanListener.onResult(wifiBean)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        wifiScanListener.onResult(wifiBean)
    }
    val wifiScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            unregisterReceiver(this)
            scanSuccess(wifiManager, wifiBean, startTime, wifiScanListener)
        }
    }
    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    registerReceiver(wifiScanReceiver, intentFilter)
    wifiManager?.startScan()
}

private fun scanSuccess(
    wifiManager: WifiManager?,
    wifiBean: WifiBean,
    startTime: Long,
    wifiScanListener: WifiScanListener
) {
    val results = wifiManager!!.scanResults
    wifiBean.isWifiScanStatus = results.size != 0
    for (scanResult in results) {
        val wifiResultBean = WifiResultBean()
        wifiResultBean.bSSID = scanResult.BSSID
        wifiResultBean.sSID = scanResult.SSID
        wifiResultBean.capabilities = scanResult.capabilities
        wifiResultBean.level = scanResult.level
        wifiBean.wifiScanResult = wifiResultBean
    }
    wifiBean.time = System.currentTimeMillis() - startTime
    wifiScanListener.onResult(wifiBean)
}