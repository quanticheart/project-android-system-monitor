package com.quanticheart.monitor.system.system.applist

import android.graphics.drawable.Drawable

class ListAppBean {

    var appName: String? = null
    var firstInstallTime: Long = 0
    var lastUpdateTime: Long = 0

    var packageName: String? = null

    var packageSign: String? = null

    var appVersionCode: Long = 0

    var appVersionName: String? = null

    var targetSdkVersion = 0

    var minSdkVersion = 0

    var description: CharSequence? = null

    var icon: Drawable? = null

    var isSystem = false
}