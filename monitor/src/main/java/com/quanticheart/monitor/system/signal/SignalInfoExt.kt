package com.quanticheart.monitor.system.signal

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.telephony.*
import android.text.TextUtils
import android.util.Log
import com.quanticheart.monitor.extentions.UNKNOWN_PARAM
import com.quanticheart.monitor.system.others.DataUtil
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import java.net.SocketException


private const val WIFI = "WIFI"
private val TAG = "SignalInfoExt"

fun Context.getNetRssiDetails(): Signal {
    val signalBean = Signal()
    try {
        val netWorkType: String = DataUtil.networkTypeALL(this)
        signalBean.type = netWorkType
        if (WIFI == netWorkType) {
            getDetailsWifiInfo(this, signalBean)
        } else {
            getMobileDbm(this, signalBean)
        }
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return signalBean
}

/**
 * mobile
 *
 * @param context
 * @return
 */
@SuppressLint("MissingPermission")
private fun getMobileDbm(context: Context, signalBean: Signal) {
    var dbm = -1
    var level = 0
    try {
        signalBean.nIpAddress = (netIPV4)
        signalBean.nIpAddressIpv6 = (netIP)
        signalBean.macAddress = getMac()
        val tm: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val cellInfoList: List<CellInfo> = tm.allCellInfo
        for (cellInfo in cellInfoList) {
            when (cellInfo) {
                is CellInfoGsm -> {
                    val cellSignalStrengthGsm: CellSignalStrengthGsm =
                        cellInfo.cellSignalStrength
                    dbm = cellSignalStrengthGsm.dbm
                    level = cellSignalStrengthGsm.level
                }
                is CellInfoCdma -> {
                    val cellSignalStrengthCdma: CellSignalStrengthCdma =
                        cellInfo.cellSignalStrength
                    dbm = cellSignalStrengthCdma.dbm
                    level = cellSignalStrengthCdma.level
                }
                is CellInfoLte -> {
                    val cellSignalStrengthLte: CellSignalStrengthLte =
                        cellInfo.cellSignalStrength
                    dbm = cellSignalStrengthLte.dbm
                    level = cellSignalStrengthLte.level
                }
                is CellInfoWcdma -> {
                    val cellSignalStrengthWcdma: CellSignalStrengthWcdma =
                        cellInfo.cellSignalStrength
                    dbm = cellSignalStrengthWcdma.dbm
                    level = cellSignalStrengthWcdma.level
                }
            }
        }
        signalBean.rssi = dbm
        signalBean.level = level
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
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
            Log.i(TAG, e.toString())
        }
        return null
    }
private val netIP: String?
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
            Log.i(TAG, e.toString())
        }
        return null
    }

/**
 * Anything worse than or equal to this will show 0 bars.
 */
private const val MIN_RSSI = -100

/**
 * Anything better than or equal to this will show the max bars.
 */
private const val MAX_RSSI = -55
private fun calculateSignalLevel(rssi: Int): Int {
    return when {
        rssi <= MIN_RSSI -> 0
        rssi >= MAX_RSSI -> 4
        else -> {
            val inputRange =
                (MAX_RSSI - MIN_RSSI).toFloat()
            val outputRange = 4.toFloat()
            ((rssi - MIN_RSSI).toFloat() * outputRange / inputRange).toInt()
        }
    }
}

/**
 * 获取WifiInfo
 *
 * @param mContext
 * @return
 */
@SuppressLint("MissingPermission")
private fun getWifiInfo(mContext: Context): WifiInfo? {
    val mWifiManager: WifiManager =
        mContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return mWifiManager.connectionInfo
}

/**
 * 是否使用代理上网
 *
 * @return
 */
private fun isWifiProxy(signalBean: Signal) {
    val proxyPort: Int
    val proxyAddress: String = System.getProperty("http.proxyHost") as String
    val portStr = System.getProperty("http.proxyPort")
    proxyPort = (portStr ?: "-1").toInt()
    if (!TextUtils.isEmpty(proxyAddress) && proxyPort != -1) {
        signalBean.isProxy = true
        signalBean.proxyAddress = proxyAddress
        signalBean.proxyPort = proxyPort.toString() + ""
    } else {
        signalBean.isProxy = false
    }
}

/**
 * wifi
 *
 * @param mContext
 * @return
 */
private fun getDetailsWifiInfo(mContext: Context, signalBean: Signal) {
    try {
        val mWifiInfo = getWifiInfo(mContext)
        val ip = mWifiInfo!!.ipAddress
        val strIp =
            "" + (ip and 0xFF) + "." + (ip shr 8 and 0xFF) + "." + (ip shr 16 and 0xFF) + "." + (ip shr 24 and 0xFF)
        signalBean.bssid = mWifiInfo.bssid
        signalBean.ssid = mWifiInfo.ssid.replace("\"", "")
        signalBean.nIpAddress = (strIp)
        signalBean.macAddress = getMac()
        signalBean.networkId = mWifiInfo.networkId
        signalBean.linkSpeed = mWifiInfo.linkSpeed.toString() + "Mbps"
        val rssi = mWifiInfo.rssi
        signalBean.rssi = rssi
        signalBean.level = calculateSignalLevel(rssi)
        isWifiProxy(signalBean)
        signalBean.supplicantState = mWifiInfo.supplicantState.name
        signalBean.nIpAddressIpv6 = (netIP)
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
}

/**
 * >=22的sdk则进行如下算法 mac
 *
 * @return
 */
private val macForBuild: String
    get() {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                if ("wlan0" == networkInterface.name) {
                    val hardwareAddress = networkInterface.hardwareAddress
                    if (hardwareAddress == null || hardwareAddress.isEmpty()) {
                        continue
                    }
                    val buf = StringBuilder()
                    for (b in hardwareAddress) {
                        buf.append(String.format("%02X:", b))
                    }
                    if (buf.isNotEmpty()) {
                        buf.deleteCharAt(buf.length - 1)
                    }
                    return buf.toString()
                }
            }
        } catch (e: SocketException) {
            Log.i(TAG, e.toString())
        }
        return UNKNOWN_PARAM
    }

private fun getMac(): String {
    return macForBuild
}