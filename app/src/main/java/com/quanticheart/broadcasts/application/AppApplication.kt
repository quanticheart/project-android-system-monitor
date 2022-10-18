package com.quanticheart.broadcasts.application

import android.app.Application
import android.content.Context
import com.quanticheart.broadcasts.receiver.MobileDataReceiver
import com.quanticheart.broadcasts.extentions.collectData
import com.quanticheart.broadcasts.extentions.updateToken
import com.quanticheart.core.system.receivers.registerBootReceiver
import com.quanticheart.monitor.alarms.AlarmActionListeners
import com.quanticheart.monitor.extentions.log
import com.quanticheart.monitor.notification.debugNotification
import com.quanticheart.monitor.project.sendDataCollected
import com.quanticheart.monitor.project.system.uuid
import com.quanticheart.sendcustonaction.monitor.receivers.boot.registerMonitor

class AppApplication : Application(), AlarmActionListeners {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
//        configAlarmActions(this)
        registerBootReceiver {
            debugNotification("Smartphone boot finish")
//            OtherCustonReceiver().register(this)
            MobileDataReceiver().register(this)
        }
//        OtherCustonReceiver().register(this)
        MobileDataReceiver().register(this)
        registerMonitor()
    }

    override fun receiveMinuteAction() {
        log("ALARM", "collectData")
        collectData()
    }

    override fun receiveHourAction() {
        val newToken = uuid
        updateToken(newToken)
    }

    override fun receiveDayAction() {
        debugNotification("receiveDayAction")
        sendDataCollected()
    }

    override fun customAction(key: String) {
        when (key) {
            "otherActionDay" -> {
                debugNotification("otherActionDay")
            }
            "otherActionHour" -> {
                debugNotification("otherActionHour")
            }
            "otherActionMinute" -> {
                log("ALARM", "otherActionMinute CUSTOM")
            }
        }
    }
}