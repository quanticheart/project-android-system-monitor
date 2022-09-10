package com.quanticheart.monitor.system.cpu

import android.os.Build
import android.text.TextUtils
import android.util.Log
import java.io.*
import java.util.regex.Pattern

private val TAG = "CpuInfo"
internal fun getCpuDetails(): Cpu {
    val cpuBean = Cpu()
    try {
        getCpuName(cpuBean)
        cpuBean.cpuFreq = curCpuFreq + "KHZ"
        cpuBean.cpuMaxFreq = maxCpuFreq + "KHZ"
        cpuBean.cpuMinFreq = minCpuFreq + "KHZ"
        cpuBean.cpuCores = heart
        cpuBean.cpuTemp = "$cpuTemp℃"
        cpuBean.cpuAbi = putCpuAbi()
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
    return cpuBean
}

private fun getCpuName(cpu: Cpu) {
    try {
        val fr = FileReader("/proc/cpuinfo")
        val br = BufferedReader(fr)
        var line: String
        try {
            while (br.readLine().also { line = it } != null) {
                val result = line.lowercase()
                val array = result.split(":\\s+".toRegex(), 2).toTypedArray()
                when {
                    array[0].startsWith("model name") -> {
                        cpu.cpuName = array[1]
                    }
                    array[0].startsWith("cpu part") -> {
                        cpu.cpuPart = array[1]
                    }
                    array[0].startsWith("hardware") -> {
                        cpu.cpuHardware = array[1]
                    }
                    array[0].startsWith("bogomips") -> {
                        cpu.bogomips = array[1]
                    }
                    array[0].startsWith("features") -> {
                        cpu.features = array[1]
                    }
                    array[0].startsWith("cpu implementer") -> {
                        cpu.cpuImplementer = array[1]
                    }
                    array[0].startsWith("cpu architecture") -> {
                        cpu.cpuArchitecture = array[1]
                    }
                    array[0].startsWith("cpu variant") -> {
                        cpu.cpuVariant = array[1]
                    }
                }
            }
        } catch (e: IOException) {
            Log.i(TAG, e.toString())
        }
    } catch (e: IOException) {
        Log.i(TAG, e.toString())
    }
}

private fun putCpuAbi(): String? {
    val abis: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Build.SUPPORTED_ABIS
    } else {
        arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
    }
    val stringBuilder = StringBuilder()
    for (abi in abis) {
        stringBuilder.append(abi)
        stringBuilder.append(",")
    }
    try {
        return stringBuilder.toString().substring(0, stringBuilder.toString().length - 1)
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
    return null
}

private val cpuTemp: String?
    get() {
        var temp: String? = null
        try {
            val fr = FileReader("/sys/class/thermal/thermal_zone9/subsystem/thermal_zone9/temp")
            val br = BufferedReader(fr)
            temp = br.readLine()
            br.close()
        } catch (e: IOException) {
            Log.i(TAG, e.toString())
        }
        return when {
            TextUtils.isEmpty(temp) -> null
            temp!!.length >= 5 -> (Integer.valueOf(
                temp
            ) / 1000).toString() + ""
            temp.length >= 4 -> (Integer.valueOf(temp) / 100).toString() + ""
            else -> temp
        }
    }
private val heart: Int
    get() {
        val cores: Int = try {
            File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).size
        } catch (e: SecurityException) {
            0
        }
        return cores
    }
private val CPU_FILTER = FileFilter { pathname -> Pattern.matches("cpu[0-9]", pathname.name) }
private val curCpuFreq: String
    get() {
        var result = "N/A"
        try {
            val fr = FileReader(
                "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"
            )
            val br = BufferedReader(fr)
            val text = br.readLine()
            result = text.trim { it <= ' ' }
        } catch (e: FileNotFoundException) {
            Log.i(TAG, e.toString())
        } catch (e: IOException) {
            Log.i(TAG, e.toString())
        }
        return result
    }
private val maxCpuFreq: String
    get() {
        var result = ""
        val cmd: ProcessBuilder
        try {
            val args = arrayOf(
                "/system/bin/cat",
                "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"
            )
            cmd = ProcessBuilder(*args)
            val process = cmd.start()
            val `in` = process.inputStream
            val re = ByteArray(24)
            while (`in`.read(re) != -1) {
                result += String(re)
            }
            `in`.close()
        } catch (ex: IOException) {
            Log.i(TAG, ex.toString())
            result = "N/A"
        }
        return result.trim { it <= ' ' }
    }
private val minCpuFreq: String
    get() {
        var result = ""
        val cmd: ProcessBuilder
        try {
            val args = arrayOf(
                "/system/bin/cat",
                "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"
            )
            cmd = ProcessBuilder(*args)
            val process = cmd.start()
            val `in` = process.inputStream
            val re = ByteArray(24)
            while (`in`.read(re) != -1) {
                result += String(re)
            }
            `in`.close()
        } catch (ex: IOException) {
            Log.i(TAG, ex.toString())
            result = "N/A"
        }
        return result.trim { it <= ' ' }
    }