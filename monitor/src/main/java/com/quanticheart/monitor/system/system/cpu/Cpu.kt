package com.quanticheart.monitor.system.system.cpu

class Cpu {
    /**
     * CPU名字
     */
    var cpuName: String? = null
    var cpuPart: String? = null
    var bogomips: String? = null
    var features: String? = null
    var cpuImplementer: String? = null
    var cpuArchitecture: String? = null
    var cpuVariant: String? = null

    /**
     * CPU频率
     */
    var cpuFreq: String? = null

    /**
     * CPU最大频率
     */
    var cpuMaxFreq: String? = null

    /**
     * CPU最小频率
     */
    var cpuMinFreq: String? = null

    /**
     * CPU硬件名
     */
    var cpuHardware: String? = null

    /**
     * CPU核数
     */
    var cpuCores = 0

    /**
     * CPU温度
     */
    var cpuTemp: String? = null

    /**
     * CPU架构
     */
    var cpuAbi: String? = null
}