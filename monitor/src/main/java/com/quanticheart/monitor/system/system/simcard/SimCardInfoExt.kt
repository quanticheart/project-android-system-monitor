package com.quanticheart.monitor.system.system.simcard

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log

private val TAG = "SimCardInfo"
fun Context.getSimDetails(): SimCard {
    val simCardBean = SimCard()
    try {
        simCardBean.isHaveCard = hasSimCard(this)
        MobCardUtils.mobGetCardInfo(this, simCardBean)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return simCardBean
}

/**
 * 判断是否包含SIM卡
 *
 * @param context 上下文
 * @return 状态 是否包含SIM卡
 */
private fun hasSimCard(context: Context): Boolean {
    var result = true
    try {
        val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        when (telMgr.simState) {
            TelephonyManager.SIM_STATE_ABSENT -> result = false
            TelephonyManager.SIM_STATE_UNKNOWN -> result = false
            else -> {}
        }
    } catch (e: Exception) {
    }
    return result
}