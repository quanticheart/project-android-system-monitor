package com.quanticheart.monitor.system.extentions

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder

inline fun <reified T> String.toModel(): T = Gson().fromJson(this, T::class.java)
inline fun <reified T> T.toJson(): String = Gson().toJson(this)

inline fun <reified T> T.logJson(tag: String = "JSON") {
    Log.e(tag, " === " + GsonBuilder().setPrettyPrinting().create().toJson(this))
}