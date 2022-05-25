package com.quanticheart.monitor.system.system.wifilist

class WifiBean {
    var isWifiScanStatus = false
    var wifiScanResult: WifiResultBean? = null
    var time: Long = 0

    class WifiResultBean {
        var sSID: String? = null
        var bSSID: String? = null
        var capabilities: String? = null
        var level = 0
    }
}