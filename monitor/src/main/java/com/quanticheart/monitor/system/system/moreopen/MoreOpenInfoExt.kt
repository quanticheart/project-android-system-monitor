package com.quanticheart.monitor.system.system.moreopen

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.quanticheart.monitor.system.system.others.CommandUtil
import com.quanticheart.monitor.system.system.others.MobileNativeHelper
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

private val TAG = "MoreOpenInfo"
fun Context.getCheckVirtualDetails(): MoreOpen {
    val moreOpenBean = MoreOpen()
    try {
        moreOpenBean.isCheckByPrivateFilePath = checkByPrivateFilePath(this)
        moreOpenBean.isCheckByMultiApkPackageName = checkByMultiApkPackageName()
        moreOpenBean.isCheckByHasSameUid = checkByHasSameUid()
        moreOpenBean.isCheckLs = MobileNativeHelper.checkMoreOpenByUid() == 1
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return moreOpenBean
}

/**
 * 检测私有路径
 *
 * @param context
 * @return
 */
private fun checkByPrivateFilePath(context: Context): Boolean {
    return try {
        val path = context.filesDir.path
        for (virtualPkg in virtualPkgs) {
            if (path.contains(virtualPkg)) {
                return true
            }
        }
        false
    } catch (e: Exception) {
        false
    }
}

// 多开App包名列表
private val virtualPkgs = arrayOf(
    "com.bly.dkplat",  //多开分身
    "com.lbe.parallel",  //平行空间
    "com.excelliance.dualaid",  //双开助手
    "com.lody.virtual",  //VirtualXposed，VirtualApp
    "com.qihoo.magic" //360分身大师
)

/**
 * maps检测
 *
 * @return
 */
private fun checkByMultiApkPackageName(): Boolean {
    var bufr: BufferedReader? = null
    try {
        bufr = BufferedReader(FileReader("/proc/self/maps"))
        var line: String
        while (bufr.readLine().also { line = it } != null) {
            for (pkg in virtualPkgs) {
                if (line.contains(pkg)) {
                    return true
                }
            }
        }
    } catch (ignore: Exception) {
    } finally {
        if (bufr != null) {
            try {
                bufr.close()
            } catch (e: IOException) {
            }
        }
    }
    return false
}

/**
 * ps检测
 *
 * @return
 */
private fun checkByHasSameUid(): Boolean {
    return try {
        val filter = uidStrFormat
        if (TextUtils.isEmpty(filter)) {
            return false
        }
        val result = CommandUtil().exec("ps")
        if (TextUtils.isEmpty(result)) {
            return false
        }
        val lines = result?.split("\n")?.toTypedArray()
        if (lines == null || lines.isEmpty()) {
            return false
        }
        var exitDirCount = 0
        for (i in lines.indices) {
            if (lines[i].contains(filter!!)) {
                val pkgStartIndex = lines[i].lastIndexOf(" ")
                val processName = lines[i].substring(
                    if (pkgStartIndex <= 0) 0 else pkgStartIndex + 1,
                    lines[i].length
                )
                val dataFile =
                    File(String.format("/data/data/%s", processName, Locale.CHINA))
                if (dataFile.exists()) {
                    exitDirCount++
                }
            }
        }
        exitDirCount > 1
    } catch (e: Exception) {
        false
    }
}

private val uidStrFormat: String?
    get() = try {
        var filter = CommandUtil().exec("cat /proc/self/cgroup")
        val uidStartIndex = filter?.lastIndexOf("uid")
        var uidEndIndex = filter?.lastIndexOf("/pid")
        if (uidEndIndex ?: 0 <= 0) {
            uidEndIndex = filter?.length
        }
        filter = filter?.substring(uidStartIndex ?: 0 + 4, uidEndIndex ?: 0)
        val strUid = filter?.replace("\n".toRegex(), "")
        if (isNumber(strUid)) {
            val uid = strUid?.let { Integer.valueOf(it) }
            String.format("u0_a%d", uid ?: 0 - 10000)
        }
        null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

private fun isNumber(str: String?): Boolean {
    if (str == null || str.isEmpty()) {
        return false
    }
    for (element in str) {
        if (!Character.isDigit(element)) {
            return false
        }
    }
    return true
}