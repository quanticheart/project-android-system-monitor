package com.quanticheart.monitor.system.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.DhcpInfo
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.util.Log

private fun getGatewayIP2(context: Context): String {
    val CM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val WiFiCheck = CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    if (WiFiCheck?.isConnected == false) {
        return "0.0.0.0"
    }
    val mainWifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val dhcp: DhcpInfo = mainWifiManager.dhcpInfo
    val ip = dhcp.gateway
    return String.format(
        "%d.%d.%d.%d",
        ip and 0xff, ip shr 8 and 0xff, ip shr 16 and 0xff, ip shr 24 and 0xff
    )
}

fun test(context: Context) {
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    val currentNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
    val linkProperties = connectivityManager.getLinkProperties(currentNetwork)
}

fun Context.systemConnectionData() {
    val stringBuilder = StringBuilder()
    val manager: ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = manager.activeNetworkInfo

    if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI &&
        networkInfo.isConnected
    ) {

    }

    //For 3G check
    val is3g =
        manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.isConnectedOrConnecting
    //For WiFi Check
    val isWifi =
        manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.isConnectedOrConnecting
    if (networkInfo?.isConnected == true) {
        Log.e("", "Wifi ok")
        stringBuilder.append(" :Conected")
    } else {
        Log.e("", "Internet disabled")
        stringBuilder.append(" :Not Conected")
    }
    if (isWifi == true) {
        Log.e("", "Connect isWifi")
        stringBuilder.append(" In wifi")
    } else if (is3g == true) {
        val networkType = networkInfo?.subtype
        when (networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> {
                Log.e("", "Connect is2g")
                stringBuilder.append(" In 2G ")
                Log.e("", "Connect is3g")
                stringBuilder.append(" In 3G ")
                Log.e("", "Connect is4g")
                stringBuilder.append(" In 4G ")
                //                default:
//                    log("Connect unknown");
//                    stringBuilder.append(" type unknown");
                stringBuilder.append(networkInfo.extraInfo)
            }
            TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> {
                Log.e("", "Connect is3g")
                stringBuilder.append(" In 3G ")
                Log.e("", "Connect is4g")
                stringBuilder.append(" In 4G ")
                stringBuilder.append(networkInfo.extraInfo)
            }
            TelephonyManager.NETWORK_TYPE_LTE -> {
                Log.e("", "Connect is4g")
                stringBuilder.append(" In 4G ")
                stringBuilder.append(networkInfo.extraInfo)
            }
        }
    } else {
        stringBuilder.append("")
    }
    val state = networkInfo?.state
    Log.e("", "$networkInfo $state")
    when (state) {
        NetworkInfo.State.CONNECTED -> {}
        NetworkInfo.State.CONNECTING -> {}
        NetworkInfo.State.SUSPENDED -> {}
        NetworkInfo.State.DISCONNECTED -> {}
        NetworkInfo.State.DISCONNECTING -> {}
        NetworkInfo.State.UNKNOWN -> {}
    }
}
