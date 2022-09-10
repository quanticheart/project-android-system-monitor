package com.quanticheart.monitor.system.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.provider.Settings
import android.util.Log
import com.quanticheart.monitor.system.others.DataUtil
import java.lang.reflect.Method

private val TAG = "NetWorkInfo"
fun Context.getNetworkDetails(): Network {
    val netWorkBean = Network()
    try {
        netWorkBean.type = DataUtil.networkTypeALL(this)
        netWorkBean.isNetworkAvailable = DataUtil.isNetworkAvailable(this)
        netWorkBean.isHaveIntent = haveIntent(this)
        netWorkBean.isFlightMode = getAirplaneMode(this)
        netWorkBean.isNFCEnabled = hasNfc(this)
        netWorkBean.isHotspotEnabled = isWifiApEnabled(this)
        val mWifiManager: WifiManager = applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        val config: WifiConfiguration = getHotPotConfig(mWifiManager)
            ?: return netWorkBean
        netWorkBean.hotspotSSID = config.SSID
        netWorkBean.hotspotPwd = config.preSharedKey
        netWorkBean.isVpn = getVpnData(this)
        netWorkBean.encryptionType =
            if (config.allowedKeyManagement.get(4)) "WPA2_PSK" else "NONE"
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return netWorkBean
}

private fun getAirplaneMode(context: Context): Boolean {
    val isAirplaneMode = Settings.System.getInt(
        context.contentResolver,
        Settings.System.AIRPLANE_MODE_ON, 0
    )
    return isAirplaneMode == 1
}

private fun hasNfc(context: Context?): Boolean {
    var bRet = false
    if (context == null) {
        return false
    }
    val manager: NfcManager = context.getSystemService(Context.NFC_SERVICE) as NfcManager
    val adapter: NfcAdapter = manager.defaultAdapter
    if (adapter.isEnabled) {
        // adapter存在，能启用
        bRet = true
    }
    return bRet
}

/**
 * 热点开关是否打开
 */
private fun isWifiApEnabled(context: Context): Boolean {
    try {
        val mWifiManager: WifiManager = context.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        val method: Method = mWifiManager.javaClass.getMethod("isWifiApEnabled")
        method.isAccessible = true
        return method.invoke(mWifiManager) as Boolean
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 获取当前热点配置
 *
 * @return
 */
private fun getHotPotConfig(mWifiManager: WifiManager): WifiConfiguration? {
    return try {
        val method: Method =
            WifiManager::class.java.getDeclaredMethod("getWifiApConfiguration")
        method.isAccessible = true
        method.invoke(mWifiManager) as WifiConfiguration
    } catch (e: Exception) {
        null
    }
}

/**
 * 是否有数据网络接入
 *
 * @param context
 * @return
 */
fun haveIntent(context: Context): Boolean {
    var mobileDataEnabled = false
    val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    try {
        val cmClass = Class.forName(cm.javaClass.name)
        val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
        method.isAccessible = true
        // get the setting for "mobile data"
        mobileDataEnabled = method.invoke(cm) as Boolean
    } catch (e: Exception) {
        // Some problem accessible private API
        // TODO do whatever error handling you want here
    }
    return mobileDataEnabled
}

fun getVpnData(context: Context): Boolean {
    return try {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.getNetworkInfo(ConnectivityManager.TYPE_VPN)?.isConnectedOrConnecting == true
    } catch (e: Exception) {
        // e.printStackTrace();
        false
    }
}