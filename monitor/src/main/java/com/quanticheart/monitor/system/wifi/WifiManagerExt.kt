package com.quanticheart.monitor.system.wifi

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.DhcpInfo
import android.net.TrafficStats
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.quanticheart.monitor.extentions.connectivityManager
import com.quanticheart.monitor.extentions.telephonyManager
import com.quanticheart.monitor.extentions.wifiManager
import java.net.*
import java.util.*


private const val megabyte = (1024 * 1024).toDouble()
private const val gigabyte = (1024 * 1024 * 1024).toDouble()

fun Context.getWifiDetails() = wifiManager.details(this)

internal fun WifiManager.details(context: Context): Wifi {
    val wifi = Wifi()

    val wInfo = connectionInfo
    val dhcp = dhcpInfo

    wifi.state = wifiState()
    wifi.ssid = wInfo.ssid
    wifi.hiddenSSID = wInfo.hiddenSSID

    if (wInfo.bssid != null) {
        wifi.bssid = wInfo.bssid.uppercase(Locale.getDefault())
    } else {
        wifi.bssid = "N/A"
    }

    wifi.ipv4 = getIPv4Address()
    wifi.ipv6 = getIPv6Address()
    wifi.ipAddress = wInfo.ipAddress

    wifi.gatewayIP = getGatewayIP(context)
    wifi.hostname = getHostname(wifi.gatewayIP ?: "0.0.0.0")

    wifi.dns1 = intToIp(dhcp.dns1)
    wifi.dns2 = intToIp(dhcp.dns2)
    wifi.subnetMask = intToIp(dhcp.netmask)
    wifi.networkId = wInfo.networkId

    // Apps cannot access MAC Address on Android 11
    wifi.macAddress = if (Build.VERSION.SDK_INT > 29) {
        "N/A"
    } else {
        getMACAddress()
    }

    wifi.networkInterface = getNetworkInterface(context)
    wifi.loopbackAddr = InetAddress.getLoopbackAddress()
    wifi.localhostAddr = getLocalhostAddress()
    wifi.leaseTime = dhcp.leaseDuration
    wifi.leaseTimeHours = dhcp.leaseDuration / 3600
    wifi.leaseTimeMinutes = dhcp.leaseDuration / 60
    wifi.freq = wInfo.frequency
    wifi.channel = convertFrequencyToChannel(wifi.freq ?: 0)
    wifi.rssi = wInfo.rssi
    wifi.rssIconv = WifiManager.calculateSignalLevel(wifi.rssi ?: 0, 101)
    wifi.networkSpeed = wInfo.linkSpeed
    wifi.txLinkSpd = 0
    wifi.rxLinkSpd = 0
    if (Build.VERSION.SDK_INT >= 29) {
        wifi.txLinkSpd = wInfo.txLinkSpeedMbps
        wifi.rxLinkSpd = wInfo.rxLinkSpeedMbps
    }

    wifi.totalRXBytes = TrafficStats.getTotalRxBytes().toDouble()
    wifi.totalTXBytes = TrafficStats.getTotalTxBytes().toDouble()
    wifi.mobileRXBytes = TrafficStats.getMobileRxBytes().toDouble()
    wifi.mobileTXBytes = TrafficStats.getMobileTxBytes().toDouble()
    wifi.wifiRXBytes = wifi.totalRXBytes ?: 0.0 - (wifi.mobileRXBytes ?: 0.0)
    wifi.wifiTXBytes = wifi.totalTXBytes ?: 0.0 - (wifi.mobileTXBytes ?: 0.0)
    wifi.wifiRXMegabytes = wifi.wifiRXBytes ?: 0.0 / megabyte
    wifi.wifiTXMegabytes = wifi.wifiTXBytes ?: 0.0 / megabyte
    wifi.wifiRXGigabytes = wifi.wifiRXBytes ?: 0.0 / gigabyte
    wifi.wifiTXGigabytes = wifi.wifiTXBytes ?: 0.0 / gigabyte
    wifi.wifiRXMegabytesStr = java.lang.String.format(Locale.US, "%.2f", wifi.wifiRXMegabytes)
    wifi.wifiTXMegabytesStr = java.lang.String.format(Locale.US, "%.2f", wifi.wifiTXMegabytes)
    wifi.wifiRXGigabytesStr = java.lang.String.format(Locale.US, "%.2f", wifi.wifiRXGigabytes)
    wifi.wifiTXGigabytesStr = java.lang.String.format(Locale.US, "%.2f", wifi.wifiTXGigabytes)
    wifi.supState = wInfo.supplicantState

    context.getTypDetails()
    getIMEIDeviceId(context)

    return wifi
}

private fun log(msg: String) {
    Log.e("COMM", msg)
}

fun getIMEIDeviceId(context: Context): String {
    val deviceId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    } else {
        val mTelephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (mTelephony.deviceId != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mTelephony.imei
            } else {
                mTelephony.deviceId
            }
        } else {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }
    log("deviceId $deviceId")
    return deviceId
}

@SuppressLint("MissingPermission")
private fun Context.getTypDetails() {
    val manager = telephonyManager

//    val manager = context.connectivityManager
//    val networkInfo = manager.activeNetworkInfo
//
//    //For 3G check
//    val is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.isConnectedOrConnecting
//    //For WiFi Check
//    val isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnectedOrConnecting
//
//    if (networkInfo != null && networkInfo.isConnected) {
//        log("Wifi ok")
//        stringBuilder.append(" :Conected")
//    } else {
//        log("Internet disabled")
//        stringBuilder.append(" :Not Conected")
//    }

    //For 3G check
    log(manager.networkOperatorName ?: "NULL")

    when (manager.dataNetworkType) {
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_IDEN,
        TelephonyManager.NETWORK_TYPE_1xRTT -> "2G"
        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_HSPAP,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_EVDO_B -> "3G"
        TelephonyManager.NETWORK_TYPE_LTE -> "4G"
        TelephonyManager.NETWORK_TYPE_NR -> "5G"
        else -> "Unknown"
    }
}

private fun WifiManager.wifiState(): String {
    val state = when (connectionInfo.supplicantState) {
        SupplicantState.COMPLETED -> "COMPLETED"
        SupplicantState.DISCONNECTED -> "DISCONNECTED"
        SupplicantState.DORMANT -> "DORMANT"
        SupplicantState.INVALID -> "INVALID"
        SupplicantState.INACTIVE -> "INACTIVE"
        SupplicantState.SCANNING -> "SCANNING"
        SupplicantState.ASSOCIATED -> "ASSOCIATED"
        SupplicantState.ASSOCIATING -> "ASSOCIATING"
        SupplicantState.UNINITIALIZED -> "UNINITIALIZED"
        SupplicantState.AUTHENTICATING -> "AUTHENTICATING"
        SupplicantState.GROUP_HANDSHAKE -> "GROUP_HANDSHAKE"
        SupplicantState.FOUR_WAY_HANDSHAKE -> "FOUR_WAY_HANDSHAKE"
        SupplicantState.INTERFACE_DISABLED -> "INTERFACE_DISABLED"
        else -> "UNDEFINED"
    }
    return "WIFI $state"
}

private fun getMACAddress(): String {
    try {
        val all: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (nif in all) {
            if (nif.name.lowercase() != "wlan0") continue
            val macBytes: ByteArray = nif.hardwareAddress ?: return ""
            val res1 = java.lang.StringBuilder()
            for (b in macBytes) {
                res1.append(String.format("%02X:", b))
            }
            if (res1.isNotEmpty()) {
                res1.deleteCharAt(res1.length - 1)
            }
            return res1.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

private fun WifiManager.getGatewayIP(context: Context): String {
    val wifiCheck = context.connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    if (wifiCheck?.isConnected == false) {
        return "0.0.0.0"
    }
    val dhcp: DhcpInfo = dhcpInfo
    val ip = dhcp.gateway
    return String.format(
        "%d.%d.%d.%d",
        ip and 0xff, ip shr 8 and 0xff, ip shr 16 and 0xff, ip shr 24 and 0xff
    )
}

private fun getIPv4Address(): String? {
    try {
        val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf: NetworkInterface = en.nextElement()
            val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return inetAddress.hostAddress
                }
            }
        }
    } catch (ex: SocketException) {
        Log.e("Wi-Fi Info", ex.toString())
    }
    return null
}

private fun getIPv6Address(): String? {
    try {
        val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf: NetworkInterface = en.nextElement()
            val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet6Address) {
                    return inetAddress.hostAddress
                }
            }
        }
    } catch (ex: SocketException) {
        Log.e("Wi-Fi Info", ex.toString())
    }
    return null
}

private fun getHostname(ip: String): String? {
    var hostname: String? = null
    try {
        val hostnameAddr = InetAddress.getByName(ip)
        hostname = hostnameAddr.hostName
        return hostname
    } catch (e: UnknownHostException) {
        e.printStackTrace()
    }
    return hostname
}

private fun getNetworkInterface(context: Context): String? {
    val cm = context.connectivityManager
    var interfc: String? = null
    for (network in cm.allNetworks) {
        val linkProp = cm.getLinkProperties(network)
        interfc = linkProp?.interfaceName
    }
    return interfc
}

private fun getLocalhostAddress(): String? {
    var localHostConverted: String? = null
    try {
        val localHost = InetAddress.getLocalHost()
        localHostConverted = localHost.toString()
        return localHostConverted
    } catch (e: UnknownHostException) {
        e.printStackTrace()
    }
    return localHostConverted
}

private fun intToIp(i: Int): String {
    return ((i and 0xFF).toString() + "."
            + (i shr 8 and 0xFF) + "."
            + (i shr 16 and 0xFF) + "."
            + (i shr 24 and 0xFF))
}

private fun convertFrequencyToChannel(freq: Int) = when (freq) {
    in 2412..2484 -> {
        (freq - 2412) / 5 + 1
    }
    in 5170..5825 -> {
        (freq - 5170) / 5 + 34
    }
    else -> {
        -1
    }
}

