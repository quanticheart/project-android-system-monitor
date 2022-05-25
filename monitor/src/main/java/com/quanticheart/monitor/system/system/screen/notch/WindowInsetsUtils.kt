package com.quanticheart.monitor.system.system.screen.notch

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.Window
import java.lang.reflect.InvocationTargetException


object WindowInsetsUtils {
    fun getNotchParams(window: Window, resultListener: ResultListener) {
        val decorView = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            decorView.post {
                var isWindowNotch = false
                var windowNotchHeight = 0
                try {
                    val displayCutout = decorView.rootWindowInsets.displayCutout
                    val rects = displayCutout!!.boundingRects
                    if (rects.size > 0) {
                        isWindowNotch = true
                        windowNotchHeight = displayCutout.safeInsetTop
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                resultListener.onResult(isWindowNotch, windowNotchHeight)
            }
        } else {
            var isWindowNotch = false
            var windowNotchHeight = 0
            if (DeviceBrandTools().isHuaWei) {
                isWindowNotch = isNotchScreenOfHuaWei(window)
                if (isWindowNotch) {
                    windowNotchHeight = getNotchHeightOfHuaWei(window)
                }
            } else if (DeviceBrandTools().isMiui) {
                isWindowNotch = isNotchScreenOfXiaoMi(window)
                if (isWindowNotch) {
                    windowNotchHeight = getNotchHeightOfXiaoMi(window)
                }
            } else if (DeviceBrandTools().isOppo) {
                isWindowNotch = isNotchScreenOfOppo(window)
                if (isWindowNotch) {
                    windowNotchHeight = getNotchHeightOfOppo(window)
                }
            } else if (DeviceBrandTools().isVivo) {
                isWindowNotch = isNotchScreenOfVivo(window)
                if (isWindowNotch) {
                    windowNotchHeight = getNotchHeightOfVivo(window)
                }
            } else if (DeviceBrandTools().isSamsung) {
                isWindowNotch = isNotchScreenOfSamsung(window)
                if (isWindowNotch) {
                    windowNotchHeight = getNotchHeightOfSamsung(window)
                }
            }
            resultListener.onResult(isWindowNotch, windowNotchHeight)
        }
    }

    /**
     * 三星是否为刘海屏
     *
     * @param window
     * @return
     */
    private fun isNotchScreenOfSamsung(window: Window?): Boolean {
        if (window == null) {
            return false
        }
        var isNotchScreen = false
        isNotchScreen = try {
            val res = window.context.resources
            val resId = res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android")
            val spec = if (resId > 0) res.getString(resId) else null
            spec != null && !TextUtils.isEmpty(spec)
        } catch (e: Exception) {
            return isNotchScreen
        }
        return isNotchScreen
    }

    /**
     * 三星刘海屏高度
     *
     * @param window
     * @return
     */
    private fun getNotchHeightOfSamsung(window: Window): Int {
        return if (!isNotchScreenOfSamsung(window)) {
            0
        } else getStatusBarHeight(window.context)
    }

    /**
     * 华为是否为刘海屏
     *
     * @param window
     * @return
     */
    private fun isNotchScreenOfHuaWei(window: Window): Boolean {
        var isNotchScreen = false
        try {
            val cl = window.context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            isNotchScreen = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isNotchScreen
    }

    /**
     * 华为刘海屏高度
     *
     * @param window
     * @return
     */
    private fun getNotchHeightOfHuaWei(window: Window): Int {
        if (!isNotchScreenOfHuaWei(window)) {
            return 0
        }
        var ret = intArrayOf(0, 0)
        try {
            val cl = window.context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("getNotchSize")
            ret = get.invoke(HwNotchSizeUtil) as IntArray
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret[1]
    }

    /**
     * XIOAMI是否为刘海屏
     *
     * @param window
     * @return
     */
    private fun isNotchScreenOfXiaoMi(window: Window): Boolean {
        try {
            return "1" == SystemProperties().get("ro.miui.notch")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * XIAOMI刘海屏高度
     *
     * @param window
     * @return
     */
    private fun getNotchHeightOfXiaoMi(window: Window): Int {
        if (!isNotchScreenOfXiaoMi(window)) {
            return 0
        }
        var result = 0
        try {
            val context = window.context
            result = if (isHideNotch(window.context)) {
                getStatusBarHeight(context)
            } else {
                getRealNotchHeight(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * OPPO是否为刘海屏
     *
     * @param window
     * @return
     */
    private fun isNotchScreenOfOppo(window: Window): Boolean {
        try {
            return window.context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * OPPO刘海屏高度
     *
     * @param window
     * @return
     */
    private fun getNotchHeightOfOppo(window: Window): Int {
        return if (!isNotchScreenOfOppo(window)) {
            0
        } else getStatusBarHeight(window.context)
    }

    /**
     * VIVO是否为刘海屏
     *
     * @param window
     * @return
     */
    private fun isNotchScreenOfVivo(window: Window?): Boolean {
        if (window == null) {
            return false
        }
        val classLoader = window.context.classLoader
        return try {
            val mClass = classLoader.loadClass("android.util.FtFeature")
            val mMethod = mClass.getMethod("isFeatureSupport", Integer.TYPE)
            mMethod.invoke(mClass, 0x00000020) as Boolean
        } catch (e: ClassNotFoundException) {
            false
        } catch (e: NoSuchMethodException) {
            false
        } catch (e: IllegalAccessException) {
            false
        } catch (e: InvocationTargetException) {
            false
        }
    }

    /**
     * VIVO刘海屏高度
     *
     * @param window
     * @return
     */
    private fun getNotchHeightOfVivo(window: Window): Int {
        return if (!isNotchScreenOfVivo(window)) {
            0
        } else getStatusBarHeight(
            window.context
        )
    }

    private fun getRealNotchHeight(context: Context): Int {
        val result: Int
        val resourceId = context.resources.getIdentifier("notch_height", "dimen", "android")
        result = if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            getStatusBarHeight(context)
        }
        return result
    }

    private fun isHideNotch(activity: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(
                activity.contentResolver,
                "force_black", 0
            ) == 1
        } else {
            true
        }
    }

    private fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resId)
        }
        return statusBarHeight
    }
}