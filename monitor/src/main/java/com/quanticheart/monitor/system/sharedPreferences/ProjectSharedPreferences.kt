@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package com.quanticheart.monitor.system.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class ProjectSharedPreferences(context: Context, name: String? = null) {

    val pref = context.getSharedPreferences(
        name ?: (context.packageName + "Prefs"),
        Context.MODE_PRIVATE
    )!!
    val editor: SharedPreferences.Editor = pref.edit()

    /**
     * put String to SharedPreference
     *
     * @param[key] key of preference
     * @param[value] value to input
     */
    fun putString(key: String, value: String) =
        editor.putString(key, value.trim()).commit()

    /**
     * get String value from SharedPreference
     *
     * @param[key] key of preference
     * if key is not presented or some unexpected problem happened, it will be return
     * @return String object
     */
    fun getString(key: String): String? {
        return getValue({ pref.getString(key, null) }, null)
    }

    fun getStringAndClear(key: String): String? {
        val k = getValue({ pref.getString(key, null) }, null)
        k?.let {
            delete(key)
        }
        return k
    }

    fun <T> putModel(key: String, value: T) =
        editor.putString(key, Gson().toJson(value).trim()).commit()

    inline fun <reified T> getModel(key: String): T {
        val k = getValue({ pref.getString(key, null) }, null)
        return Gson().fromJson(k, T::class.java)
    }

    /**
     * delete key-value from SharedPreference
     * @param[key] key to delete
     */
    fun delete(key: String) = editor.remove(key).commit()

    /**
     * clear all of preferences
     */
    fun clear() = editor.clear().commit()

    inline fun <R> getValue(block: () -> R, def: Any?): R =
        try {
            block()
        } catch (e: Exception) {
            def as R
        }
}
