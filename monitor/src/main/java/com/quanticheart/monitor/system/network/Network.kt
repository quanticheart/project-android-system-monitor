package com.quanticheart.monitor.system.network

class Network {
    /**
     * 网络类型
     */
    var type: String? = null

    /**
     * 网络是否可用
     */
    var isNetworkAvailable = false

    /**
     * 是否开启数据流量
     */
    var isHaveIntent = false

    /**
     * 是否是飞行模式
     */
    var isFlightMode = false

    /**
     * NFC功能是否开启
     */
    var isNFCEnabled = false

    /**
     * 是否开启热点
     */
    var isHotspotEnabled = false

    /**
     * 热点账号
     */
    var hotspotSSID: String? = null

    /**
     * 热点密码
     */
    var hotspotPwd: String? = null

    /**
     * 热点加密类型
     */
    var encryptionType: String? = null
    var isVpn = false
}