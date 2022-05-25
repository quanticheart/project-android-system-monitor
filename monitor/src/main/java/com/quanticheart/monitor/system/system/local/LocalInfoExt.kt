package com.quanticheart.monitor.system.system.local

import android.util.Log
import java.util.*

private val TAG = "LocalInfo"
internal val localDetails: Local
    get() {
        val localBean = Local()
        try {
            localBean.country = Locale.getDefault().country
            localBean.language = Locale.getDefault().language
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return localBean
    }