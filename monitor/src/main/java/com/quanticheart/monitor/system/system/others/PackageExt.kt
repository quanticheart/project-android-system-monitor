package com.quanticheart.monitor.system.system.others

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import java.security.MessageDigest


@SuppressLint("PackageManagerGetSignatures")
private fun getRawSignature(paramContext: Context, paramString: String?): Array<Signature>? {
    if (paramString == null || paramString.isEmpty()) {
        return null
    }
    val localPackageManager = paramContext.packageManager
    val localPackageInfo: PackageInfo?
    try {
        localPackageInfo =
            localPackageManager.getPackageInfo(paramString, PackageManager.GET_SIGNATURES)
        if (localPackageInfo == null) {
            return null
        }
    } catch (localNameNotFoundException: PackageManager.NameNotFoundException) {
        return null
    }
    return localPackageInfo.signatures
}

internal fun getSign(context: Context, packageName: String?): String? {
    val arrayOfSignature = getRawSignature(context, packageName)
    return if (arrayOfSignature == null || arrayOfSignature.isEmpty()) {
        null
    } else getMessageDigest(arrayOfSignature[0].toByteArray())
}

private fun getMessageDigest(paramArrayOfByte: ByteArray): String? {
    val arrayOfChar1 = charArrayOf(
        48.toChar(),
        49.toChar(),
        50.toChar(),
        51.toChar(),
        52.toChar(),
        53.toChar(),
        54.toChar(),
        55.toChar(),
        56.toChar(),
        57.toChar(),
        97.toChar(),
        98.toChar(),
        99.toChar(),
        100.toChar(),
        101.toChar(),
        102.toChar()
    )
    try {
        val localMessageDigest = MessageDigest.getInstance("MD5")
        localMessageDigest.update(paramArrayOfByte)
        val arrayOfByte = localMessageDigest.digest()
        val i = arrayOfByte.size
        val arrayOfChar2 = CharArray(i * 2)
        var j = 0
        var k = 0
        while (true) {
            if (j >= i) {
                return String(arrayOfChar2)
            }
            val m = arrayOfByte[j].toInt()
            val n = k + 1
            arrayOfChar2[k] = arrayOfChar1[0xF and m ushr 4]
            k = n + 1
            arrayOfChar2[n] = arrayOfChar1[m and 0xF]
            j++
        }
    } catch (e: java.lang.Exception) {
    }
    return null
}
