package com.quanticheart.monitor.system.xposed

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log


private val TAG = "XposedHookInfo"

/**
 * 检测是否有需要的关键字
 * 如果有 则表示APP被劫持
 * 当发现有Xposed工具时，下面数据显示为：
 * "xposedInfo":{//Xposed的详细信息
 * "xposedApp"："flase",//是否hook了本APP
 * "xposedImei"："flase",//是否hook了IMEI号
 * "xposedSerial"："flase",//是否hook了序列号
 * "xposedSsid"："flase",//是否hook了SSID
 * "xposedMac"："flase",//是否hook了本MAC地址
 * "xposedAddress"："flase",//是否hook了蓝牙地址
 * "xposedAndroidId"："flase",//是否hook了AndroidId
 * "xposedImsi"："flase",//是否hook了IMSI
 * "xposedLatitude"："flase",//是否hook了纬度
 * "xposedLongitude"："flase"//是否hook了经度
 * }
 *
 * @return
 */
internal fun Context.xposedInject(): XposedHook {
    val xposedHookBean = XposedHook()
    val packageName = getPackageName(this)
    try {
        val classLoader = ClassLoader.getSystemClassLoader()
        val clsXposedHtlpers =
            classLoader.loadClass("de.robv.android.xposed.XposedHelpers").newInstance()
        packageName?.let { checkKeyWordInFiled(clsXposedHtlpers, "fieldCache", xposedHookBean, it) }
        packageName?.let {
            checkKeyWordInFiled(
                clsXposedHtlpers, "methodCache", xposedHookBean,
                it
            )
        }
        packageName?.let {
            checkKeyWordInFiled(
                clsXposedHtlpers,
                "constructorCache",
                xposedHookBean,
                it
            )
        }
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return xposedHookBean
}

/**
 * 关键字 只是表明hook了这些函数 并不能代表使用
 * getDeviceId 修改IMEI号
 * SERIAL  修改序列号
 * getSSID 修改WIFI名称
 * getMacAddress 修改 WIFI MAC地址
 * BluetoothAdapter#getAddress 修改蓝牙MAC地址
 * Secure#getString 修改androidId
 * getSubscriberId 修改IMSI
 * getLatitude 纬度
 * getLongitude 经度
 *
 * @param cls
 * @param fieldName
 * @return
 */
private fun checkKeyWordInFiled(
    cls: Any,
    fieldName: String,
    xposedHook: XposedHook,
    packageName: String
): Boolean {
    val map: Map<*, *>
    try {
        val field = cls.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        map = field[cls] as Map<*, *>
        if (!map.isEmpty()) {
            for (aKeySet in map.keys) {
                if (aKeySet.toString().contains(packageName)) {
                    xposedHook.isXposedApp = true
                }
                if (aKeySet.toString().toLowerCase()
                        .contains("getDeviceId".toLowerCase())
                ) {
                    xposedHook.isXposedImei = true
                }
                if (aKeySet.toString().toLowerCase().contains("SERIAL".toLowerCase())) {
                    xposedHook.isXposedSerial = true
                }
                if (aKeySet.toString().toLowerCase().contains("getSSID".toLowerCase())) {
                    xposedHook.isXposedSsid = true
                }
                if (aKeySet.toString().toLowerCase()
                        .contains("getMacAddress".toLowerCase())
                ) {
                    xposedHook.isXposedMac = true
                }
                if (aKeySet.toString().toLowerCase()
                        .contains("BluetoothAdapter#getAddress".toLowerCase())
                ) {
                    xposedHook.isXposedAddress = true
                }
                if (aKeySet.toString().toLowerCase()
                        .contains("Secure#getString".toLowerCase())
                ) {
                    xposedHook.isXposedAndroidId = true
                }
                if (aKeySet.toString().toLowerCase()
                        .contains("getSubscriberId".toLowerCase())
                ) {
                    xposedHook.isXposedImsi = true
                }
                if (aKeySet.toString().toLowerCase()
                        .contains("getLatitude".toLowerCase())
                ) {
                    xposedHook.isXposedLatitude = true
                }
                if (aKeySet.toString().toLowerCase()
                        .contains("getLongitude".toLowerCase())
                ) {
                    xposedHook.isXposedLongitude = true
                }
            }
        }
    } catch (e: Exception) {
    }
    return false
}

private fun getPackageName(context: Context): String? {
    val packageInfo: PackageInfo
    val packageManager = context.packageManager
    try {
        packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        if (packageInfo != null) {
            return packageInfo.packageName
        }
    } catch (e: Exception) {
    }
    return null
}