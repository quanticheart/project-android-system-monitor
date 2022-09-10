package com.quanticheart.monitor.system.screen

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.quanticheart.monitor.system.screen.notch.ResultListener
import com.quanticheart.monitor.system.screen.notch.WindowInsetsUtils

private val TAG = "ScreenInfo"
fun Context.getMobScreen(window: Window?): Screen {
    val screenBean = Screen()
    try {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        screenBean.densityScale = displayMetrics.density
        screenBean.densityDpi = displayMetrics.densityDpi
        screenBean.width = displayMetrics.widthPixels
        screenBean.height = displayMetrics.heightPixels
        val screenMode = Settings.System.getInt(
            contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE
        )
        val screenBrightness = Settings.System.getInt(
            contentResolver,
            Settings.System.SCREEN_BRIGHTNESS
        )
        val screenChange = Settings.System.getInt(
            contentResolver,
            Settings.System.ACCELEROMETER_ROTATION
        )
        screenBean.isScreenAuto = screenMode == 1
        screenBean.screenBrightness = screenBrightness
        screenBean.isScreenAutoChange = screenChange == 1
        screenBean.isCheckHasNavigationBar = checkHasNavigationBar(this)
        screenBean.isCheckHideStatusBar = checkHideStatusBar(this)
        screenBean.getStatusBarHeight = getStatusBarHeight(this)
        screenBean.getNavigationBarHeight = getNavigationBarHeight(this)
        if (window != null) {
            WindowInsetsUtils.getNotchParams(window, object : ResultListener {
                override fun onResult(isWindowNotch: Boolean, windowNotchHeight: Int) {
                    screenBean.isWindowNotch = isWindowNotch
                    screenBean.windowNotchHeight = windowNotchHeight
                }
            })
        }
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
    return screenBean
}

private fun checkHideStatusBar(context: Context): Boolean {
    return checkFullScreenByTheme(context) || checkFullScreenByCode(context) || checkFullScreenByCode2(
        context
    )
}

private fun checkFullScreenByTheme(context: Context): Boolean {
    val theme: Resources.Theme? = context.theme
    if (theme != null) {
        val typedValue = TypedValue()
        val result: Boolean =
            theme.resolveAttribute(R.attr.windowFullscreen, typedValue, false)
        if (result) {
            typedValue.coerceToString()
            if (typedValue.type == TypedValue.TYPE_INT_BOOLEAN) {
                return typedValue.data != 0
            }
        }
    }
    return false
}

private fun checkFullScreenByCode(context: Context): Boolean {
    if (context is Activity) {
        val window: Window = context.window
        if (window != null) {
            val decorView = window.decorView
            if (decorView != null) {
                return decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
    }
    return false
}

private fun checkFullScreenByCode2(context: Context): Boolean {
    return if (context is Activity) {
        context.window
            .attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
    } else false
}

private fun checkHasNavigationBar(context: Context): Boolean {
    if (context is Activity) {
        val windowManager: WindowManager = context.windowManager
        val d: Display = windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics)
        }
        val realHeight: Int = realDisplayMetrics.heightPixels
        val realWidth: Int = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)
        val displayHeight: Int = displayMetrics.heightPixels
        val displayWidth: Int = displayMetrics.widthPixels
        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    }
    return false
}

private fun getStatusBarHeight(context: Context): Int {
    val resources = context.resources
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

private fun getNavigationBarHeight(context: Context): Int {
    val resources = context.resources
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}