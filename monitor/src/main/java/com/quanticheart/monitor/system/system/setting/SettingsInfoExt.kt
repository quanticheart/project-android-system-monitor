package com.quanticheart.monitor.system.system.setting

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log

private val TAG = "SettingsInfo"

@SuppressLint("HardwareIds")
internal fun Context.getSettingsDetails(): com.quanticheart.monitor.system.system.setting.Settings {
    val settingsBean = com.quanticheart.monitor.system.system.setting.Settings()
    try {
        settingsBean.androidId =
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        settingsBean.screenOffTimeout =
            Settings.System.getString(contentResolver, "screen_off_timeout")
        settingsBean.soundEffectsEnabled =
            Settings.System.getString(contentResolver, "sound_effects_enabled")
        settingsBean.screenBrightnessMode =
            Settings.System.getString(contentResolver, "screen_brightness_mode")
        settingsBean.developmentSettingsEnabled = Settings.Secure.getString(
            contentResolver,
            "development_settings_enabled"
        )
        settingsBean.accelerometerRotation =
            Settings.System.getString(contentResolver, "accelerometer_rotation")
        settingsBean.lockPatternVisiblePattern = Settings.System.getString(
            contentResolver,
            "lock_pattern_visible_pattern"
        )
        settingsBean.lockPatternAutolock =
            Settings.System.getString(contentResolver, "lock_pattern_autolock")
        settingsBean.usbMassStorageEnabled =
            Settings.System.getString(contentResolver, "usb_mass_storage_enabled")
        settingsBean.allowMockLocation = isAllowMockLocation(this)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return settingsBean
}

/**
 * 判断是否打开了允许虚拟位置,如果打开了 则弹窗让他去关闭
 */
private fun isAllowMockLocation(context: Context): Boolean {
    try {
        var isOpen = Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.ALLOW_MOCK_LOCATION,
            0
        ) != 0
        /**
         * 该判断API是androidM以下的API,由于Android M中已经没有了关闭允许模拟位置的入口,所以这里一旦检测到开启了模拟位置,并且是android M以上,则
         * 默认设置为未有开启模拟位置
         */
        if (isOpen && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            isOpen = false
        }
        return isOpen
    } catch (e: Exception) {
    }
    return false
}