package com.quanticheart.monitor.system.simcard

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.util.regex.Pattern

@SuppressLint("HardwareIds")
internal fun Context.getMeid(): String? {
    val telephonyManager =
        getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var meid: String? = null
    try {
        @SuppressLint("MissingPermission") val deviceId = telephonyManager.deviceId
        if (!TextUtils.isEmpty(deviceId)) {
            if (!isNumeric(deviceId)) {
                meid = deviceId
            }
        }
        try {
            val getMeid = telephonyManager.javaClass.getDeclaredMethod("getMeid")
            getMeid.isAccessible = true
            val meid8 = getMeid.invoke(telephonyManager) as String
            if (!TextUtils.isEmpty(meid8)) {
                if (!isNumeric(meid8)) {
                    meid = meid8
                }
            }
        } catch (e: Exception) {
            val result = chooseImeiOrMeid(telephonyManager)
            if (!TextUtils.isEmpty(result)) {
                meid = result
            }
        }
    } catch (e: Exception) {
    }
    return meid
}

private fun chooseImeiOrMeid(telephonyManager: TelephonyManager): String? {
    var imei0: String? = null
    var imei1: String? = null
    try {
        imei0 = getSIMOperator(telephonyManager, "getDeviceId", 0)
    } catch (e1: Exception) {
        try {
            imei0 = getSIMOperator(telephonyManager, "getDeviceIdGemini", 0)
        } catch (e2: Exception) {
        }
    }
    try {
        imei1 = getSIMOperator(telephonyManager, "getDeviceId", 1)
    } catch (e1: Exception) {
        try {
            imei1 = getSIMOperator(telephonyManager, "getDeviceIdGemini", 1)
        } catch (e2: Exception) {
        }
    }
    return verifyImeiOrMeid(imei0, imei1)
}

private fun verifyImeiOrMeid(imei0: String?, imei1: String?): String? {
    return if (!TextUtils.isEmpty(imei0) && !TextUtils.isEmpty(imei1)) {
        val imei0IsNum = isNumeric(imei0)
        val imei1IsNum = isNumeric(imei1)
        if (imei0IsNum) if (imei1IsNum) null else imei1 else imei0
    } else {
        null
    }
}

private val pattern = Pattern.compile("[0-9]*")
fun isNumeric(str: String?): Boolean {
    return str?.let { pattern.matcher(it).matches() } == true
}

/**
 * 获取卡的imei信息
 */
private fun getSIMOperator(
    telephony: TelephonyManager,
    predictedMethodName: String,
    slotID: Int
): String? {
    var imei: String? = null
    try {
        val telephonyClass = Class.forName(telephony.javaClass.name)
        val parameter = arrayOfNulls<Class<*>?>(1)
        parameter[0] = Int::class.javaPrimitiveType
        val getSimStateGemini = telephonyClass.getMethod(predictedMethodName, *parameter)
        val obParameter = arrayOfNulls<Any>(1)
        obParameter[0] = slotID
        val phone = getSimStateGemini.invoke(telephony, *obParameter)
        if (phone != null) {
            imei = phone.toString()
        }
    } catch (e: Exception) {
        throw Exception(predictedMethodName)
    }
    return imei
}
