package com.quanticheart.monitor.system.alarms

interface AlarmActionListeners {
    fun receiveMinuteAction()
    fun receiveHourAction()
    fun receiveDayAction()
    fun customAction(key: String)
}