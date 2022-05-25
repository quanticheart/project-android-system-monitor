package com.quanticheart.monitor.system.system.wifilist

interface WifiScanListener {
    fun onResult(jsonObject: WifiBean?)
}