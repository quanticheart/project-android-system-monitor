package com.quanticheart.monitor.system.memory

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.text.format.Formatter
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.Method
import java.util.*

private val TAG = "MemoryInfo"

/**
 * info
 *
 * @return
 */
internal fun Context.getMemoryDetails(): Memory {
    val memoryBean = Memory()
    try {
        memoryBean.ramMemory = getTotalMemory(this)
        memoryBean.ramAvailMemory = getAvailMemory(this)
        memoryBean.romMemoryAvailable = getRomSpace(this)
        memoryBean.romMemoryTotal = getRomSpaceTotal(this)
        memoryBean.sdCardMemoryAvailable = getSdcardSize(this)
        memoryBean.sdCardMemoryTotal = getSdcardSizeTotal(this)
        memoryBean.sdCardRealMemoryTotal = getRealStorage(this)
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
    return memoryBean
}

/**
 * total
 *
 * @param context
 * @return
 */
private fun getTotalMemory(context: Context): String {
    val str1 = "/proc/meminfo"
    val str2: String
    val arrayOfString: Array<String>
    var initial_memory: Long = 0
    try {
        val localFileReader = FileReader(str1)
        val localBufferedReader = BufferedReader(localFileReader, 8192)
        str2 = localBufferedReader.readLine()
        arrayOfString = str2.split("\\s+").toTypedArray()
        initial_memory = java.lang.Long.valueOf(arrayOfString[1]) * 1024
        localBufferedReader.close()
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
    return Formatter.formatFileSize(context, initial_memory)
}

/**
 * 获取android当前可用内存大小
 *
 * @param context
 * @return
 */
private fun getAvailMemory(context: Context): String {
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val mi = ActivityManager.MemoryInfo()
    am.getMemoryInfo(mi)
    //mi.availMem; 当前系统的可用内存
    return Formatter.formatFileSize(context, mi.availMem)
}

/**
 * rom
 *
 * @param context
 * @return
 */
private fun getRomSpace(context: Context): String {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSize.toLong()
    val availableBlocks = stat.availableBlocks.toLong()
    return Formatter.formatFileSize(context, availableBlocks * blockSize)
}

private fun getRomSpaceTotal(context: Context): String {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSize.toLong()
    val totalBlocks = stat.blockCount.toLong()
    return Formatter.formatFileSize(context, totalBlocks * blockSize)
}

/**
 * sd is null ==rom
 *
 * @param context
 * @return
 */
private fun getSdcardSize(context: Context): String {
    val path = Environment.getExternalStorageDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSize.toLong()
    val availableBlocks = stat.availableBlocks.toLong()
    return Formatter.formatFileSize(context, availableBlocks * blockSize)
}

private fun getSdcardSizeTotal(context: Context): String {
    val path = Environment.getExternalStorageDirectory()
    val stat = StatFs(path.path)
    val blockCount = stat.blockCount.toLong()
    val blockSize = stat.blockSize.toLong()
    return Formatter.formatFileSize(context, blockCount * blockSize)
}

@SuppressLint("SoonBlockedPrivateApi")
private fun getRealStorage(context: Context): String? {
    var total = 0L
    try {
        val storageManager =
            context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val version = Build.VERSION.SDK_INT
        val unit: Float = if (version >= Build.VERSION_CODES.O) 1000f else 1024.toFloat()
        if (version < Build.VERSION_CODES.M) {
            val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumeList")
            val volumeList = getVolumeList.invoke(storageManager) as Array<StorageVolume>
            var getPathFile: Method? = null
            for (volume in volumeList) {
                if (getPathFile == null) {
                    getPathFile = volume.javaClass.getDeclaredMethod("getPathFile")
                }
                val file = getPathFile!!.invoke(volume) as File
                total += file.totalSpace
            }
        } else {
            val getVolumes = StorageManager::class.java.getDeclaredMethod("getVolumes")
            val getVolumeInfo = getVolumes.invoke(storageManager) as List<Any>
            for (obj in getVolumeInfo) {
                val getType = obj.javaClass.getField("type")
                val type = getType.getInt(obj)
                if (type == 1) {
                    var totalSize = 0L
                    if (version >= Build.VERSION_CODES.O) {
                        val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                        val fsUuid = getFsUuid.invoke(obj) as String
                        totalSize = getTotalSize(context, fsUuid)
                    } else if (version >= Build.VERSION_CODES.N_MR1) {
                        val getPrimaryStorageSize =
                            StorageManager::class.java.getMethod("getPrimaryStorageSize")
                        totalSize = getPrimaryStorageSize.invoke(storageManager) as Long
                    }
                    val isMountedReadable =
                        obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = isMountedReadable.invoke(obj) as Boolean
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as File
                        if (totalSize == 0L) {
                            totalSize = f.totalSpace
                        }
                        total += totalSize
                    }
                } else if (type == 0) {
                    val isMountedReadable =
                        obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = isMountedReadable.invoke(obj) as Boolean
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as File
                        total += f.totalSpace
                    }
                }
            }
        }
        return getUnit(total.toFloat(), unit)
    } catch (ignore: Exception) {
    }
    return null
}

private val units = arrayOf("B", "KB", "MB", "GB", "TB")

/**
 * 进制转换
 */
fun getUnit(sizes: Float, base: Float): String {
    var size = sizes
    var index = 0
    while (size > base && index < 4) {
        size /= base
        index++
    }
    return String.format(Locale.getDefault(), "%.2f %s ", size, units[index])
}

@SuppressLint("NewApi")
fun getTotalSize(context: Context, fsUuid: String?): Long {
    return try {
        val id: UUID = if (fsUuid == null) {
            StorageManager.UUID_DEFAULT
        } else {
            UUID.fromString(fsUuid)
        }
        val stats = context.getSystemService(
            StorageStatsManager::class.java
        )
        stats.getTotalBytes(id)
    } catch (e: NoSuchFieldError) {
        e.printStackTrace()
        -1
    } catch (e: NoClassDefFoundError) {
        e.printStackTrace()
        -1
    } catch (e: NullPointerException) {
        e.printStackTrace()
        -1
    } catch (e: IOException) {
        e.printStackTrace()
        -1
    }
}