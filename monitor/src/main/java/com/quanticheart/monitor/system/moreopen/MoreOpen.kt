package com.quanticheart.monitor.system.moreopen

class MoreOpen {
    /**
     * 检测私有路径判断是否有多开
     */
    var isCheckByPrivateFilePath = false

    /**
     * maps检测
     */
    var isCheckByMultiApkPackageName = false

    /**
     * ps检测
     */
    var isCheckByHasSameUid = false
    var isCheckLs = false
}