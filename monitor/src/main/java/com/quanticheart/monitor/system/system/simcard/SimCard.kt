package com.quanticheart.monitor.system.system.simcard


class SimCard {
    /**
     * imei  for sim1
     */
    var sim1Imei: String? = null

    /**
     * imei for sim2
     */
    var sim2Imei: String? = null

    /**
     * imsi for sim1
     */
    var sim1Imsi: String? = null

    /**
     * imsi for sim2
     */
    var sim2Imsi: String? = null

    /**
     * 有流量的卡的卡槽id
     */
    var simSlotIndex = -1

    /**
     * meid
     */
    var meid: String? = null

    /**
     * 卡1运营商
     */
    var sim1ImsiOperator: String? = null

    /**
     * 卡2运营商
     */
    var sim2ImsiOperator: String? = null

    /**
     * 卡1是否激活
     */
    var isSim1Ready = false

    /**
     * 卡2是否激活
     */
    var isSim2Ready = false

    /**
     * 是否有两张卡
     */
    var isTwoCard = false

    /**
     * 是否有卡
     */
    var isHaveCard = false

    /**
     * 流量卡运营商
     */
    var operator: String? = null
    var sim1IccId: String? = null
    var sim2IccId: String? = null
    var sim1SimId = -1
    var sim2SimId = -1
    var sim1subId = -1
    var sim2subId = -1
    var sim1mcc: String? = null
    var sim2mcc: String? = null
    var sim1mnc: String? = null
    var sim2mnc: String? = null
    var sim1carrierName: String? = null
    var sim2carrierName: String? = null
}