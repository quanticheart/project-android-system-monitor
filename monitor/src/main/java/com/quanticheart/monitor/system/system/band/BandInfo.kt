package com.quanticheart.monitor.system.system.band

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.quanticheart.monitor.system.system.others.MobileNativeHelper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

private val TAG = "bandInfo"

/**
 * bandInfo
 *
 * @return
 */
internal fun getBandDetails(): Band {
    val bandBean = Band()
    try {
        bandBean.baseBand = baseBand
        bandBean.innerBand = inner
        val linuxBand = linuxKernelInfo
        bandBean.linuxBand =
            if (TextUtils.isEmpty(linuxBand)) System.getProperty("os.version") else linuxBand
        bandBean.detailLinuxBand = MobileNativeHelper.kennel()
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return bandBean
}

/**
 * BASEBAND-VER
 * return String
 */
private val baseBand: String
    get() {
        var version: String? = null
        try {
            @SuppressLint("PrivateApi") val cl = Class.forName("android.os.SystemProperties")
            val invoker = cl.newInstance()
            val m = cl.getMethod("get", String::class.java, String::class.java)
            val result = m.invoke(invoker, "gsm.version.baseband", "no message")
            version = result as String
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
        return if (TextUtils.isEmpty(version)) Build.getRadioVersion() else version!!
    }

/**
 * INNER-VER
 * return String
 */
private val inner: String
    get() {
        val ver: String =
            if (null != Build.VERSION.INCREMENTAL && Build.DISPLAY.contains(Build.VERSION.INCREMENTAL)) {
                Build.DISPLAY
            } else {
                Build.VERSION.INCREMENTAL
            }
        return ver
    }

/***
 * Android Linux
 */
private val linuxKernelInfo: String?
    get() {
        var process: Process? = null
        val mLinuxKernal: String
        try {
            process = Runtime.getRuntime().exec("cat /proc/version")
        } catch (e: IOException) {
            Log.i(TAG, e.toString())
        }
        if (process != null) {
            val outs = process.inputStream
            val isrout = InputStreamReader(outs)
            val brout = BufferedReader(isrout, 8 * 1024)
            val result = StringBuilder()
            var line: String
            // get the whole standard output string
            try {
                while (brout.readLine().also { line = it } != null) {
                    result.append(line)
                    // result += "\n";
                }
            } catch (e: IOException) {
                Log.i(TAG, e.toString())
            }
            if ("" != result.toString()) {
                val keyword = "version "
                var index = result.indexOf(keyword)
                line = result.substring(index + keyword.length)
                index = line.indexOf(" ")
                mLinuxKernal = line.substring(0, index)
                return mLinuxKernal
            }
        }
        return null
    }