package com.quanticheart.monitor.system.project.system

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.DhcpInfo
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.util.Log
import com.quanticheart.monitor.system.extentions.UNKNOWN_PARAM
import com.quanticheart.monitor.system.extentions.connectivityManager
import com.quanticheart.monitor.system.extentions.telephonyManager
import com.quanticheart.monitor.system.extentions.wifiManager
import com.quanticheart.monitor.system.project.model.SimpleMobileDetails
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import java.net.SocketException

/*
2022-05-22 18:44:34.150 3478-5559/com.quanticheart.broadcasts E/APP:: details: {"bssid":"02:15:B2:00:01:00","channel":8,"dns1":"10.0.2.3","dns2":"0.0.0.0","freq":2447,"gatewayIP":"192.168.232.1","hiddenSSID":false,"hostname":"192.168.232.1","ipAddress":48801984,"ipv4":"192.168.232.2","ipv6":"fe80::15:b2ff:fe00:0%wlan0","leaseTime":1476526080,"leaseTimeHours":410146,"leaseTimeMinutes":24608768,"localhostAddr":"localhost/127.0.0.1","loopbackAddr":"::1","macAddress":"02:15:B2:00:00:00","mobileRXBytes":21121.0,"mobileTXBytes":26181.0,"networkId":0,"networkInterface":"wlan0","networkSpeed":6,"rssIconv":100,"rssi":-50,"rxLinkSpd":0,"ssid":"\"AndroidWifi\"","state":"WIFI COMPLETED","subnetMask":"0.0.0.0","supState":"COMPLETED","totalRXBytes":4.2362156E7,"totalTXBytes":937945.0,"txLinkSpd":0,"wifiRXBytes":4.2362156E7,"wifiRXGigabytes":4.2362156E7,"wifiRXGigabytesStr":"42362156.00","wifiRXMegabytes":4.2362156E7,"wifiRXMegabytesStr":"42362156.00","wifiTXBytes":937945.0,"wifiTXGigabytes":937945.0,"wifiTXGigabytesStr":"937945.00","wifiTXMegabytes":937945.0,"wifiTXMegabytesStr":"937945.00"}
2022-05-22 18:44:34.153 3478-5559/com.quanticheart.broadcasts E/APP:: network: {"isFlightMode":false,"isHaveIntent":true,"isHotspotEnabled":false,"isNFCEnabled":false,"isNetworkAvailable":true,"isVpn":false,"type":"WIFI"}
2022-05-22 18:44:34.158 3478-5559/com.quanticheart.broadcasts E/APP:: net: {"bssid":"02:15:b2:00:01:00","isProxy":false,"level":4,"linkSpeed":"6Mbps","macAddress":"02:15:B2:00:00:00","nIpAddress":"192.168.232.2","networkId":0,"rssi":-50,"ssid":"AndroidWifi","type":"WIFI"}
2022-05-22 18:44:34.220 3478-5559/com.quanticheart.broadcasts E/INFO: {"app":{"appName":"Broadcasts","appVersionCode":2,"appVersionName":"2.0","firstInstallTime":"2022/05/22 18:43:21","lastUpdateTime":"2022/05/22 18:43:21","minSdkVersion":24,"packageName":"com.quanticheart.broadcasts","targetSdkVersion":31},"battery":{"charge":"notCharging","percentage":100.0,"plugged":"not plugged"},"connection":{"dns":"0.0.0.0","dns2":"0.0.0.0","gatewayIP":"192.168.232.2","ipv4":"192.168.232.2","ipv6":"fe80::15:b2ff:fe00:0%wlan0","type":"wifi"},"date":"2022/05/22 18:44:34","hardware":{"brand":"google","device":"generic_x86_arm","incremental":"5875966","manufacturer":"Google","model":"AOSP on IA Emulator","releaseVersion":"9","sdkInt":28},"location":{"enable":true,"latitude":0.0,"longitude":0.0,"permissionsGranted":true,"systemCountry":"US","systemLanguage":"en"},"screen":"on"}
 */

private const val tag = "getNetworkInfo"

internal fun Context.getNetworkInfo(): SimpleMobileDetails.Connection {
    val conn = SimpleMobileDetails.Connection()
    try {
        when (getConnectionType()) {
            WIFI -> {
                conn.type = "wifi"
                conn.speed = "Broadband"
                getDetailsWifiInfo(conn)
            }
            CELLULAR -> {
                conn.type = "cellular"
                conn.speed = getCellularSpeedType()
                getMobileDbm(conn)
            }
            VPN -> {
                conn.type = "vpn"
                conn.speed = UNKNOWN_PARAM
                getDetailsWifiInfo(conn)
            }
        }
    } catch (e: Exception) {
        Log.e("getNetworkInfo", e.toString())
    }
    return conn
}

private const val UNKNOWN_CONN = -1
private const val WIFI = 1
private const val CELLULAR = 2
private const val VPN = 3
private fun Context.getConnectionType() = connectivityManager.run {
    getNetworkCapabilities(activeNetwork)?.run {
        when {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> WIFI
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> CELLULAR
            hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> VPN
            else -> UNKNOWN_CONN
        }
    }
}

/**
 * mobile
 *
 * @return
 */
@SuppressLint("MissingPermission")
private fun getMobileDbm(conn: SimpleMobileDetails.Connection) {
    try {
        conn.ipv4 = netIPV4 ?: UNKNOWN_PARAM
        conn.ipv6 = netIPV6 ?: UNKNOWN_PARAM
    } catch (e: Exception) {
        Log.i(tag, e.toString())
    }
}

/**
 * wifi
 *
 * @return
 */
private fun Context.getDetailsWifiInfo(conn: SimpleMobileDetails.Connection) = wifiManager.run {
    try {
        val dhcp = dhcpInfo
        conn.ipv4 = netIPV4 ?: UNKNOWN_PARAM
        conn.ipv6 = netIPV6 ?: UNKNOWN_PARAM
        conn.gatewayIP = getGatewayIP(this@getDetailsWifiInfo)

        conn.dns1 = intToIp(dhcp.dns1)
        conn.dns2 = intToIp(dhcp.dns2)
    } catch (e: Exception) {
        Log.i(tag, e.toString())
    }
}

/**
 *
 */

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

private val netIPV4: String?
    get() {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (e: SocketException) {
            Log.i(tag, e.toString())
        }
        return null
    }
private val netIPV6: String?
    get() {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet6Address && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (e: SocketException) {
            Log.i(tag, e.toString())
        }
        return null
    }

private fun intToIp(i: Int): String {
    return ((i and 0xFF).toString() + "."
            + (i shr 8 and 0xFF) + "."
            + (i shr 16 and 0xFF) + "."
            + (i shr 24 and 0xFF))
}

@SuppressLint("MissingPermission")
private fun Context.getCellularSpeedType() = telephonyManager.run {
    return@run when (dataNetworkType) {
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
        else -> UNKNOWN_PARAM
    }
}
