package com.quanticheart.monitor.system.emulator

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.io.*
import java.util.*

/**
 * log TAG
 */
private val TAG = "EmulatorInfo"
fun Context.getEmulatorDetails(): Emulator {
    val emulatorBean = Emulator()
    try {
        val checkBuild = isEmulatorAbsoluly
        val checkPkg = isPkgInstalled(this, "com.example.android.apis") && isPkgInstalled(
            this,
            "com.android.development"
        )
        val checkPipes = checkPipes()
        val checkQEmuDriverFile =
            checkQEmuDriverFile("/proc/tty/drivers") || checkQEmuDriverFile("/proc/cpuinfo")
        val checkCpuInfo = readCpuInfo()
        emulatorBean.isCheckBuild = checkBuild
        emulatorBean.isCheckPkg = checkPkg
        emulatorBean.isCheckPipes = checkPipes
        emulatorBean.isCheckQEmuDriverFile = checkQEmuDriverFile
        emulatorBean.isCheckCpuInfo = checkCpuInfo
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
    return emulatorBean
}

private fun readCpuInfo(): Boolean {
    var result = "unknown"
    try {
        val args = arrayOf("/system/bin/cat", "/proc/cpuinfo")
        val cmd = ProcessBuilder(*args)
        val process = cmd.start()
        val sb = StringBuilder()
        var readLine: String?
        val responseReader = BufferedReader(InputStreamReader(process.inputStream, "utf-8"))
        while (responseReader.readLine().also { readLine = it } != null) {
            sb.append(readLine)
        }
        responseReader.close()
        result = sb.toString().lowercase(Locale.getDefault())
    } catch (ex: Exception) {
        Log.i(TAG, ex.toString())
    }
    return result.contains("intel") || result.contains("amd")
}

private val KNOWN_QEMU_DRIVERS = arrayOf(
    "goldfish"
)

private fun checkQEmuDriverFile(name: String): Boolean {
    val driverFile = File(name)
    if (driverFile.exists() && driverFile.canRead()) {
        val data = ByteArray(1024)
        try {
            val inStream: InputStream = FileInputStream(driverFile)
            inStream.read(data)
            inStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val driverData = String(data)
        for (knownQemuDriver in KNOWN_QEMU_DRIVERS) {
            if (driverData.contains(knownQemuDriver)) {
                return true
            }
        }
    }
    return false
}

private val KNOWN_PIPES = arrayOf(
    "/dev/socket/qemud",
    "/dev/qemu_pipe"
)

private fun checkPipes(): Boolean {
    for (pipes in KNOWN_PIPES) {
        val qemuSocket = File(pipes)
        if (qemuSocket.exists()) {
            return true
        }
    }
    return false
}

private fun isPkgInstalled(context: Context, pkgName: String): Boolean {
    val packageInfo: PackageInfo? = try {
        context.packageManager.getPackageInfo(pkgName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
    return packageInfo != null
}

private val isEmulatorAbsoluly: Boolean
    get() = if (Build.PRODUCT.contains("sdk") ||
        Build.PRODUCT.contains("sdk_x86") ||
        Build.PRODUCT.contains("sdk_google") ||
        Build.PRODUCT.contains("Andy") ||
        Build.PRODUCT.contains("Droid4X") ||
        Build.PRODUCT.contains("nox") ||
        Build.PRODUCT.contains("vbox86p") ||
        Build.PRODUCT.contains("aries")
    ) {
        true
    } else if (Build.MANUFACTURER.contains("Genymotion") ||
        Build.MANUFACTURER.contains("Andy") ||
        Build.MANUFACTURER.contains("nox") ||
        Build.MANUFACTURER.contains("TiantianVM")
    ) {
        true
    } else if (Build.BRAND.contains("Andy")) {
        true
    } else if (Build.DEVICE.contains("Andy") ||
        Build.DEVICE.contains("Droid4X") ||
        Build.DEVICE.contains("nox") ||
        Build.DEVICE.contains("vbox86p") ||
        Build.DEVICE.contains("aries")
    ) {
        true
    } else if (Build.MODEL.contains("Emulator") ||
        Build.MODEL.contains("google_sdk") ||
        Build.MODEL.contains("Droid4X") ||
        Build.MODEL.contains("TiantianVM") ||
        Build.MODEL.contains("Andy") ||
        Build.MODEL.contains("Android SDK built for x86_64") ||
        Build.MODEL.contains("Android SDK built for x86")
    ) {
        true
    } else if (Build.HARDWARE.contains("vbox86") ||
        Build.HARDWARE.contains("nox") ||
        Build.HARDWARE.contains("ttVM_x86")
    ) {
        true
    } else Build.FINGERPRINT.contains("generic/sdk/generic") ||
            Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86") ||
            Build.FINGERPRINT.contains("Andy") ||
            Build.FINGERPRINT.contains("ttVM_Hdragon") ||
            Build.FINGERPRINT.contains("generic/google_sdk/generic") ||
            Build.FINGERPRINT.contains("vbox86p") ||
            Build.FINGERPRINT.contains("generic/vbox86p/vbox86p")