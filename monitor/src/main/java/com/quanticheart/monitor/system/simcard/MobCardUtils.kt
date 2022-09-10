package com.quanticheart.monitor.system.simcard

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


object MobCardUtils {
    private val TAG = MobCardUtils::class.java.simpleName
    private const val CM_MOBILE1 = "46000"
    private const val CM_MOBILE2 = "46002"
    private const val CM_MOBILE3 = "46004"
    private const val CM_MOBILE4 = "46007"
    private const val CU_MOBILE1 = "46001"
    private const val CU_MOBILE2 = "46006"
    private const val CU_MOBILE3 = "46009"
    private const val CT_MOBILE1 = "46003"
    private const val CT_MOBILE2 = "46005"
    private const val CT_MOBILE3 = "46011"

    /**
     * 获取网络运营商，CM是移动，CU是联通，CT是电信
     *
     * @param data str
     * @return str
     */
    private fun getOperators(data: String): String? {
        return if (!TextUtils.isEmpty(data)) {
            if (data.startsWith(CM_MOBILE1) || data.startsWith(CM_MOBILE2) || data.startsWith(
                    CM_MOBILE3
                ) || data.startsWith(CM_MOBILE4)
            ) {
                "CM"
            } else if (data.startsWith(CU_MOBILE1) || data.startsWith(CU_MOBILE2) || data.startsWith(
                    CU_MOBILE3
                )
            ) {
                "CU"
            } else if (data.startsWith(CT_MOBILE1) || data.startsWith(CT_MOBILE2) || data.startsWith(
                    CT_MOBILE3
                )
            ) {
                "CT"
            } else {
                null
            }
        } else {
            null
        }
    }

    /**
     * mobile info
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    fun mobGetCardInfo(context: Context, simCardBean: SimCard) {
        val telephonyManager: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simStub = getDefaultDataSub(context)
        var sim1 = 0
        var sim2 = 1
        if (simStub == 2) {
            sim1 = 1
            sim2 = 2
        }
        simCardBean.simSlotIndex = simStub
        var sim1Ready = telephonyManager.simState == TelephonyManager.SIM_STATE_READY
        var sim2Ready = false
        try {
            sim1Ready = getSIMStateBySlot(telephonyManager, "getSimStateGemini", sim1)
            sim2Ready = getSIMStateBySlot(telephonyManager, "getSimStateGemini", sim2)
        } catch (e: MobException) {
            try {
                sim1Ready = getSIMStateBySlot(telephonyManager, "getSimState", sim1)
                sim2Ready = getSIMStateBySlot(telephonyManager, "getSimState", sim2)
            } catch (e1: MobException) {
                Log.i(TAG, e1.toString())
            }
        }
        simCardBean.isSim1Ready = sim1Ready
        simCardBean.isSim2Ready = sim2Ready
        simCardBean.isTwoCard = sim1Ready && sim2Ready
        var sim1Imei: String? = null
        var sim2Imei: String? = null
        try {
            sim1Imei = getSIMOperator(telephonyManager, "getDeviceIdGemini", sim1)
            sim2Imei = getSIMOperator(telephonyManager, "getDeviceIdGemini", sim2)
        } catch (e: MobException) {
            try {
                sim1Imei = getSIMOperator(telephonyManager, "getDeviceId", sim1)
                sim2Imei = getSIMOperator(telephonyManager, "getDeviceId", sim2)
            } catch (e1: MobException) {
                Log.i(TAG, e1.toString())
            }
        }
        if (!TextUtils.isEmpty(sim1Imei)) {
            simCardBean.sim1Imei = if (isNumeric(sim1Imei)) sim1Imei else null
        }
        if (!TextUtils.isEmpty(sim2Imei)) {
            simCardBean.sim2Imei = if (isNumeric(sim2Imei)) sim2Imei else null
        }
        var sim1Imsi: String? = null
        var sim2Imsi: String? = null
        try {
            sim1Imsi = getSIMOperator(telephonyManager, "getSubscriberIdGemini", sim1)
            sim2Imsi = getSIMOperator(telephonyManager, "getSubscriberIdGemini", sim2)
        } catch (e: MobException) {
            try {
                sim1Imsi = getSIMOperator(telephonyManager, "getSubscriberId", sim1)
                sim2Imsi = getSIMOperator(telephonyManager, "getSubscriberId", sim2)
            } catch (e1: MobException) {
                Log.i(TAG, e1.toString())
            }
        }
        simCardBean.sim1Imsi = sim1Imsi
        simCardBean.sim2Imsi = sim2Imsi
        var sim1Operator: String? = null
        var sim2Operator: String? = null
        try {
            sim1Operator =
                getOperators(getSIMOperator(telephonyManager, "getSimOperatorGemini", sim1))
            sim2Operator =
                getOperators(getSIMOperator(telephonyManager, "getSimOperatorGemini", sim2))
        } catch (e: MobException) {
            try {
                sim1Operator =
                    getOperators(getSIMOperator(telephonyManager, "getSimOperator", sim1))
                sim2Operator =
                    getOperators(getSIMOperator(telephonyManager, "getSimOperator", sim2))
            } catch (e1: MobException) {
                Log.i(TAG, e1.toString())
            }
        }
        simCardBean.meid = context.getMeid()
        simInfoQuery(context, simCardBean)
        if (TextUtils.isEmpty(sim1Operator)) {
            if (TextUtils.isEmpty(simCardBean.sim1carrierName)) {
                simCardBean.sim1ImsiOperator = simCardBean.sim1IccId?.let { getIccidOperators(it) }
            } else {
                simCardBean.sim1ImsiOperator = simCardBean.sim1carrierName?.let {
                    getCarrierOperators(
                        it
                    )
                }
            }
        } else {
            simCardBean.sim1ImsiOperator = sim1Operator
        }
        if (TextUtils.isEmpty(sim2Operator)) {
            if (TextUtils.isEmpty(simCardBean.sim2carrierName)) {
                simCardBean.sim2ImsiOperator = simCardBean.sim2IccId?.let { getIccidOperators(it) }
            } else {
                simCardBean.sim2ImsiOperator = simCardBean.sim2carrierName?.let {
                    getCarrierOperators(
                        it
                    )
                }
            }
        } else {
            simCardBean.sim2ImsiOperator = sim2Operator
        }
        simCardBean.operator =
            if (simCardBean.simSlotIndex == 0) simCardBean.sim1ImsiOperator else simCardBean.sim2ImsiOperator
        if (TextUtils.isEmpty(simCardBean.operator)) {
            var operator: String = telephonyManager.simOperator
            if (TextUtils.isEmpty(operator) && Build.VERSION.SDK_INT <= 28 && checkPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {
                var thisOperator: String? = null
                try {
                    thisOperator = telephonyManager.subscriberId
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (!TextUtils.isEmpty(thisOperator) && thisOperator!!.length >= 5) {
                    operator = thisOperator.substring(0, 5)
                }
            }
            simCardBean.operator = getOperators(operator)
        }
    }

    private fun checkPermission(context: Context, permission: String): Boolean {
        val packageManager: PackageManager? = context.packageManager
        return packageManager != null && PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(
            permission,
            context.packageName
        )
    }

    private fun getIccidOperators(data: String): String? {
        return if (!TextUtils.isEmpty(data)) {
            if (data.startsWith("898600") || data.startsWith("898602") || data.startsWith("898604") || data.startsWith(
                    "898607"
                )
            ) {
                "CM"
            } else if (data.startsWith("898601") || data.startsWith("898606") || data.startsWith("898609")) {
                "CU"
            } else if (data.startsWith("898603") || data.startsWith("898611")) {
                "CT"
            } else {
                null
            }
        } else {
            null
        }
    }

    private fun getCarrierOperators(data: String): String? {
        return if (!TextUtils.isEmpty(data)) {
            when {
                data.startsWith("中国移动") -> {
                    "CM"
                }
                data.startsWith("中国联通") -> {
                    "CU"
                }
                data.startsWith("中国电信") -> {
                    "CT"
                }
                else -> {
                    null
                }
            }
        } else {
            null
        }
    }

    private fun simInfoQuery(context: Context, simCardBean: SimCard) {
        val uri = Uri.parse("content://telephony/siminfo")
        var cursor: Cursor? = null
        val var4: ContentResolver = context.contentResolver
        try {
            cursor = var4.query(
                uri,
                arrayOf("_id", "icc_id", "sim_id", "mcc", "mnc", "carrier_name", "number"),
                "sim_id>=?",
                arrayOf("0"),
                null as String?
            )
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    val iccId = cursor.getString(cursor.getColumnIndexOrThrow("icc_id"))
                    val simId = cursor.getInt(cursor.getColumnIndexOrThrow("sim_id"))
                    val subId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
                    val mcc = cursor.getString(cursor.getColumnIndexOrThrow("mcc"))
                    val mnc = cursor.getString(cursor.getColumnIndexOrThrow("mnc"))
                    val carrierName = cursor.getString(cursor.getColumnIndexOrThrow("carrier_name"))
                    if (simId == 0) {
                        simCardBean.sim1IccId = iccId
                        simCardBean.sim1SimId = simId
                        simCardBean.sim1subId = subId
                        simCardBean.sim1mcc = mcc
                        simCardBean.sim1mnc = mnc
                        simCardBean.sim1carrierName = carrierName
                    } else if (simId == 1) {
                        simCardBean.sim2IccId = iccId
                        simCardBean.sim2SimId = simId
                        simCardBean.sim2subId = subId
                        simCardBean.sim2mcc = mcc
                        simCardBean.sim2mnc = mnc
                        simCardBean.sim2carrierName = carrierName
                    }
                }
            }
        } catch (var15: Exception) {
            var15.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    /**
     * 判断卡是否已经准备好
     *
     * @param predictedMethodName
     * @param slotID
     * @return
     */
    private fun getSIMStateBySlot(
        telephony: TelephonyManager,
        predictedMethodName: String,
        slotID: Int
    ): Boolean {
        var isReady = false
        try {
            val telephonyClass = Class.forName(telephony.javaClass.name)
            val parameter = arrayOfNulls<Class<*>?>(1)
            parameter[0] = Int::class.javaPrimitiveType
            val getSimStateGemini = telephonyClass.getMethod(predictedMethodName, *parameter)
            val obParameter = arrayOfNulls<Any>(1)
            obParameter[0] = slotID
            val ob_phone = getSimStateGemini.invoke(telephony, *obParameter)
            if (ob_phone != null) {
                val simState = ob_phone.toString().toInt()
                if (simState == TelephonyManager.SIM_STATE_READY) {
                    isReady = true
                }
            }
        } catch (e: Exception) {
            throw MobException(predictedMethodName)
        }
        return isReady
    }

    /**
     * 获取哪张卡开启的运营商
     *
     * @param context con
     * @return int
     */
    private fun getDefaultDataSub(context: Context): Int {
        var num = -1
        val sm: SubscriptionManager = SubscriptionManager.from(context)
        try {
            val getSubId: Method =
                sm.javaClass.getDeclaredMethod("getDefaultDataSubscriptionId")
            try {
                num = getSubId.invoke(sm) as Int
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        } catch (e: NoSuchMethodException) {
            try {
                @SuppressLint("PrivateApi") val getSubId: Method =
                    sm.javaClass.getDeclaredMethod("getDefaultDataSubId")
                try {
                    num = getSubId.invoke(sm) as Int
                } catch (e1: IllegalAccessException) {
                    e1.printStackTrace()
                } catch (e1: InvocationTargetException) {
                    e1.printStackTrace()
                }
            } catch (e1: NoSuchMethodException) {
                /**
                 * 新加一个方案，此方案也是用于拿到获取的运营商，跟getDefaultDataSubscriptionId平级
                 */
                try {
                    val slot: Method = sm.javaClass.getMethod("getDefaultDataSubscriptionInfo")
                    try {
                        val subscriptionInfo: SubscriptionInfo =
                            slot.invoke(sm) as SubscriptionInfo
                        num = subscriptionInfo.simSlotIndex
                    } catch (e2: IllegalAccessException) {
                        e2.printStackTrace()
                    } catch (e2: InvocationTargetException) {
                        e2.printStackTrace()
                    }
                } catch (e3: NoSuchMethodException) {
                    e3.printStackTrace()
                }
            }
        }
        return num
    }

    /**
     * 获取卡的imei信息
     */
    private fun getSIMOperator(
        telephony: TelephonyManager,
        predictedMethodName: String,
        slotID: Int
    ): String {
        var imei = "unknown"
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
            throw MobException(predictedMethodName)
        }
        return imei
    }
}