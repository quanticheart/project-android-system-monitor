package com.quanticheart.monitor.system.system.bluetooth

class Bluetooth {
    var bluetoothAddress: String? = null
    var isEnabled = false
    var device: List<Device>? = null
    var phoneName: String? = null

    class Device {
        var address: String? = null
        var name: String? = null
    }
}