package com.quanticheart.monitor.system.screen.notch

import java.lang.reflect.Method


class SystemProperties {
    private fun getClass(name: String): Class<*>? {
        try {
            return Class.forName(name)
        } catch (e: ClassNotFoundException) {
            try {
                return ClassLoader.getSystemClassLoader().loadClass(name)
            } catch (e1: ClassNotFoundException) {
            }
        }
        return null
    }

    private fun getMethod(clz: Class<*>?): Method? {
        val method: Method? = try {
            clz?.getMethod("get", String::class.java)
        } catch (e: Exception) {
            null
        }
        return method
    }

    operator fun get(key: String?): String {
        if (key == null) {
            return ""
        }
        val value: String?
        return try {
            value = (if (getStringProperty != null) getStringProperty!!.invoke(
                null,
                key
            ) else null) as String?
            value?.trim { it <= ' ' } ?: ""
        } catch (var4: Exception) {
            ""
        }
    }

    companion object {
        private var getStringProperty: Method? = null
        private var sSystemProperties: SystemProperties? = null
        val instance: SystemProperties?
            get() {
                if (sSystemProperties == null) {
                    synchronized(SystemProperties::class.java) {
                        if (sSystemProperties == null) {
                            sSystemProperties = SystemProperties()
                        }
                    }
                }
                return sSystemProperties
            }
    }

    init {
        getStringProperty = getMethod(getClass("android.os.SystemProperties"))
    }
}