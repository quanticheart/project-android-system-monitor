package com.quanticheart.monitor.system.system.screen


class Screen {
    /**
     * 是否是刘海屏
     */
    var isWindowNotch = false

    /**
     * 刘海屏高度
     */
    var windowNotchHeight = 0

    /**
     * 当前屏幕密度与标准屏幕密度的比值
     */
    var densityScale = 0f

    /**
     * 屏幕密度
     */
    var densityDpi = 0

    /**
     * 屏幕宽度
     */
    var width = 0

    /**
     * 屏幕高度
     */
    var height = 0

    /**
     * 亮度是否为自动调节
     */
    var isScreenAuto = false

    /**
     * 屏幕亮度
     */
    var screenBrightness = 0

    /**
     * 屏幕是否开启自动旋转
     */
    var isScreenAutoChange = false

    /**
     * 是否隐藏状态栏
     */
    var isCheckHideStatusBar = false

    /**
     * 是否显示底部导航栏
     */
    var isCheckHasNavigationBar = false

    /**
     * 获取状态栏高度
     */
    var getStatusBarHeight = 0

    /**
     * 获取底部导航栏的高度
     */
    var getNavigationBarHeight = 0
}