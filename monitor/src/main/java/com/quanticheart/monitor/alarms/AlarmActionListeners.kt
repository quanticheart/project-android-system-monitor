package com.quanticheart.monitor.alarms

interface AlarmActionListeners {
    fun receiveMinuteAction()
    fun receiveHourAction()
    fun receiveDayAction()
    fun customAction(key: String)
}