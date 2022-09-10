package com.quanticheart.monitor.system.wifi

import android.net.wifi.SupplicantState
import java.net.InetAddress
import java.util.*

class Wifi {
    var state: String? = null
    var ssid: String? = null
    var bssid: String? = null
    var hiddenSSID: Boolean = false
    var ipv4: String? = null
    var ipv6: String? = null
    var gatewayIP: String? = null
    var hostname: String? = null
    var dns1: String? = null
    var dns2: String? = null
    var subnetMask: String? = null
    var networkId: Int? = null
    var ipAddress: Int? = null
    var macAddress: String? = null

    var networkInterface: String? = null
    var loopbackAddr: InetAddress? = null
    var localhostAddr: String? = null
    var leaseTime: Int? = null
    var leaseTimeHours: Int? = null
    var leaseTimeMinutes: Int? = null
    var freq: Int? = null
    var channel: Int? = null
    var rssi: Int? = null
    var rssIconv: Int? = null
    var networkSpeed: Int? = null
    var txLinkSpd: Int? = null
    var rxLinkSpd: Int? = null

    var totalRXBytes: Double? = null
    var totalTXBytes: Double? = null
    var mobileRXBytes: Double? = null
    var mobileTXBytes: Double? = null
    var wifiRXBytes: Double? = null
    var wifiTXBytes: Double? = null
    var wifiRXMegabytes: Double? = null
    var wifiTXMegabytes: Double? = null
    var wifiRXGigabytes: Double? = null
    var wifiTXGigabytes: Double? = null
    var wifiRXMegabytesStr: String? = null
    var wifiTXMegabytesStr: String? = null
    var wifiRXGigabytesStr: String? = null
    var wifiTXGigabytesStr: String? = null
    var supState: SupplicantState? = null

    fun getLog(): String {
        val s = StringBuilder()
        s.append("STATE: $state")
        s.append(" :: SSID: $ssid")
        s.append(" :: BSSID: " + bssid?.uppercase(Locale.getDefault()))
        s.append(" :: hiddenSSID: $hiddenSSID")
        s.append(" :: IPv4: $ipv4")
        s.append(" :: IPv6: $ipv6")
        s.append(" :: Gateway IP: $gatewayIP")
        s.append(" :: Hostname: $hostname")
        s.append(" :: DNS (1): $dns1")
        s.append(" :: DNS (2): $dns2")
        s.append(" :: Subnet Mask: $subnetMask")
        s.append(" :: Network ID: $networkId")
        s.append(" :: MAC Address: $macAddress")
        s.append(" :: Network Interface: $networkInterface")
        s.append(" :: Loopback Address: $loopbackAddr")
        s.append(" :: Localhost: $localhostAddr")
        s.append(" :: Frequency: " + freq + "MHz")
        s.append(" :: Network Channel: $channel")
        s.append(" :: RSSI (Signal Strength): " + rssIconv + "%" + " (" + rssi + "dBm" + ")")
        s.append(" :: Lease Duration: " + leaseTime + "s " + "(" + leaseTimeHours + "h)")
        s.append(" :: Lease Duration: " + leaseTime + "s " + "(" + leaseTimeMinutes + "m)")
        s.append(" :: Transmit Link Speed: " + txLinkSpd + "MB/s")
        s.append(" :: Receive Link Speed: " + rxLinkSpd + "MB/s")
        s.append(" :: Network Speed: " + networkSpeed + "MB/s")
        s.append(" :: Transmitted MBs/GBs: " + wifiTXMegabytesStr + "MB " + "(" + wifiTXGigabytesStr + "GB" + ")")
        s.append(" :: Received MBs/GBs: " + wifiRXMegabytesStr + "MB " + "(" + wifiRXGigabytesStr + "GB" + ")")
        s.append(" :: Supplicant State: $supState")
        return s.toString()
    }
}