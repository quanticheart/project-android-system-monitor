package com.quanticheart.monitor.system.system.battery

class Battery {
    /**
     * 电量
     */
    var br: String? = null

    /**
     * 电池状态
     */
    var status: String? = null

    /**
     * 电池充电状态
     */
    var plugState: String? = null

    /**
     * 电池健康状况
     */
    var health: String? = null

    /**
     * 是否有电池
     */
    var isPresent = false

    /**
     * 电池的技术制造
     */
    var technology: String? = null

    /**
     * 电池温度
     */
    var temperature: String? = null

    /**
     * 电池电压
     */
    var voltage: String? = null

    /**
     * 电池总电量
     */
    var power: String? = null
}