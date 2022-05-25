package com.quanticheart.monitor.system.system.debug

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Debug
import android.os.Process
import android.provider.Settings
import android.util.Log
import java.io.BufferedReader
import java.io.FileReader

private val TAG = "DebugInfo"

/**
 * 调试模式判断
 *
 * @return
 */
internal fun Context.getDebugDetails(): com.quanticheart.monitor.system.system.debug.Debug {
    val debugBean = Debug()
    try {
        debugBean.isOpenDebug = isOpenDebug(this)
        debugBean.isDebugVersion = checkIsDebugVersion(this)
        debugBean.isDebugging = checkIsDebuggerConnected()
        debugBean.isReadProcStatus = readProcStatus()
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return debugBean
}

/**
 * 开启了调试模式
 *
 * @param context
 * @return
 */
private fun isOpenDebug(context: Context): Boolean {
    try {
        return Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.ADB_ENABLED,
            0
        ) > 0
    } catch (e: Exception) {
    }
    return false
}

/**
 * 判斷是debug版本
 *
 * @param context
 * @return
 */
private fun checkIsDebugVersion(context: Context): Boolean {
    try {
        return (context.applicationInfo.flags
                and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    } catch (e: Exception) {
    }
    return false
}

/**
 * 是否正在调试
 *
 * @return
 */
private fun checkIsDebuggerConnected(): Boolean {
    try {
        return Debug.isDebuggerConnected()
    } catch (e: Exception) {
    }
    return false
}

/**
 * 读取TracePid
 *
 * @return
 */
private fun readProcStatus(): Boolean {
    return try {
        val localBufferedReader =
            BufferedReader(FileReader("/proc/" + Process.myPid() + "/status"))
        var tracerPid = ""
        while (true) {
            val str = localBufferedReader.readLine()
            if (str!!.contains("TracerPid")) {
                tracerPid = str.substring(str.indexOf(":") + 1, str.length).trim { it <= ' ' }
                break
            }
        }
        localBufferedReader.close()
        "0" != tracerPid
    } catch (fuck: Exception) {
        false
    }
}