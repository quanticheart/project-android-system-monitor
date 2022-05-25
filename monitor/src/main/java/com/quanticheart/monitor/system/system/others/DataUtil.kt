package com.quanticheart.monitor.system.system.others

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import com.quanticheart.monitor.system.extentions.UNKNOWN_PARAM
import com.quanticheart.monitor.system.extentions.connectivityManager

@SuppressLint("MissingPermission")
object DataUtil {

    fun networkTypeALL(context: Context): String {
        val manager = context.connectivityManager
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            return "WIFI"
        }
        val telephonyManager: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
            TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
            TelephonyManager.NETWORK_TYPE_LTE -> "4G"
            else -> UNKNOWN_PARAM
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val mgr: ConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val networks: Array<Network> = mgr.allNetworks
                var networkInfo: NetworkInfo?
                for (mNetwork in networks) {
                    networkInfo = mgr.getNetworkInfo(mNetwork)
                    if (networkInfo?.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            } else {
                val info: Array<NetworkInfo> = mgr.allNetworkInfo
                for (anInfo in info) {
                    if (anInfo.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            return true
        }
        return false
    }
}