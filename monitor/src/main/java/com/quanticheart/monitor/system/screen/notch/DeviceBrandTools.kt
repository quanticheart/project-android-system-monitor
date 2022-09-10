package com.quanticheart.monitor.system.screen.notch

import android.os.Build
import android.text.TextUtils


class DeviceBrandTools {
    val isHuaWei: Boolean
        get() {
            val manufacturer = Build.MANUFACTURER
            if (!TextUtils.isEmpty(manufacturer)) {
                if (manufacturer.contains("HUAWEI")) {
                    return true
                }
            }
            return false
        }
    val isMiui: Boolean
        get() {
            val manufacturer = getSystemProperty("ro.miui.ui.version.name")
            return !TextUtils.isEmpty(manufacturer)
        }
    val isOppo: Boolean
        get() {
            val manufacturer = Build.MANUFACTURER
            return "oppo".equals(manufacturer, ignoreCase = true)
        }
    val isVivo: Boolean
        get() {
            val manufacturer = getSystemProperty("ro.vivo.os.name")
            return !TextUtils.isEmpty(manufacturer)
        }
    val isSamsung: Boolean
        get() {
            val manufacturer = Build.MANUFACTURER
            return "samsung".equals(manufacturer, ignoreCase = true)
        }

    private fun getSystemProperty(propName: String): String {
        return SystemProperties()[propName]
    }

    companion object {
        private var sDeviceBrandTools: DeviceBrandTools? = null
        val instance: DeviceBrandTools?
            get() {
                if (sDeviceBrandTools == null) {
                    synchronized(DeviceBrandTools::class.java) {
                        if (sDeviceBrandTools == null) {
                            sDeviceBrandTools = DeviceBrandTools()
                        }
                    }
                }
                return sDeviceBrandTools
            }
    }
}