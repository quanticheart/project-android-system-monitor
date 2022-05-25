package com.quanticheart.monitor.system.system.sdcard

import android.content.Context
import android.os.Environment
import android.os.storage.StorageManager
import android.text.TextUtils
import android.util.Log
import java.lang.reflect.Array
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

private val TAG = "SDCardInfoExt"
val Context.sdCard: SDCard
    get() {
        val sdCardBean = SDCard()
        try {
            sdCardBean.isSDCardEnable = isSDCardEnableByEnvironment
            sdCardBean.sDCardPath = (sDCardPathByEnvironment)
            val extendedMemoryPath =
                getExtendedMemoryPath(this)
            sdCardBean.isExtendedMemory = !TextUtils.isEmpty(extendedMemoryPath)
            sdCardBean.extendedMemoryPath = extendedMemoryPath
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return sdCardBean
    }
private val sDCardPathByEnvironment: String?
    get() = if (isSDCardEnableByEnvironment) {
        Environment.getExternalStorageDirectory().absolutePath
    } else null
private val isSDCardEnableByEnvironment: Boolean
    get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

private fun getExtendedMemoryPath(mContext: Context): String? {
    val mStorageManager: StorageManager =
        mContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
    var storageVolumeClazz: Class<*>? = null
    try {
        storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
        val getVolumeList: Method = mStorageManager.javaClass.getMethod("getVolumeList")
        val getPath = storageVolumeClazz.getMethod("getPath")
        val isRemovable = storageVolumeClazz.getMethod("isRemovable")
        val result = getVolumeList.invoke(mStorageManager)
        val length = Array.getLength(result)
        for (i in 0 until length) {
            val storageVolumeElement = Array.get(result, i)
            val path = getPath.invoke(storageVolumeElement) as String
            val removable = isRemovable.invoke(storageVolumeElement) as Boolean
            if (removable) {
                return path
            }
        }
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
    return null
}