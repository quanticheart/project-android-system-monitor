package com.quanticheart.monitor.system.app

class Package {
    var appName: String? = null
    var launcherAppName: String? = null
    var firstInstallTime: Long = 0
    var lastUpdateTime: Long = 0
    var packageName: String? = null
    var packageSign: String? = null
    var appVersionCode: Long = 0
    var appVersionName: String? = null
    var targetSdkVersion = 0
    var minSdkVersion = 0
    var description: CharSequence? = null
}