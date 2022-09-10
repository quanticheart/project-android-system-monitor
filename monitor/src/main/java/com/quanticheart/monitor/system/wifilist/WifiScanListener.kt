package com.quanticheart.monitor.system.wifilist

interface WifiScanListener {
    fun onResult(jsonObject: WifiBean?)
}