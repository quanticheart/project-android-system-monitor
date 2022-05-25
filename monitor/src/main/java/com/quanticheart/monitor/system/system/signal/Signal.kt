package com.quanticheart.monitor.system.system.signal

class Signal {
    /**
     * 网络类型
     */
    var type: String? = null

    /**
     * bssid
     */
    var bssid: String? = null

    /**
     * ssid
     */
    var ssid: String? = null

    /**
     * ipv4
     */
    var nIpAddress: String? = null

    /**
     * ipv6
     */
    var nIpAddressIpv6: String? = null

    /**
     * mac地址
     */
    var macAddress: String? = null

    /**
     * 网络id
     */
    var networkId = 0

    /**
     * 网络速度
     */
    var linkSpeed: String? = null

    /**
     * 信号强度
     */
    var rssi = 0

    /**
     * 信号等级
     */
    var level = 0

    /**
     * 连接状态
     */
    var supplicantState: String? = null

    /**
     * 是否开启代理
     */
    var isProxy = false

    /**
     * 代理地址
     */
    var proxyAddress: String? = null

    /**
     * 代理端口号
     */
    var proxyPort: String? = null
}