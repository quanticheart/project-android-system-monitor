package com.quanticheart.broadcasts

import android.app.Application
import android.content.Context
import com.quanticheart.core.system.receivers.registerBootReceiver
import com.quanticheart.monitor.system.alarms.AlarmActionListeners
import com.quanticheart.monitor.system.extentions.log
import com.quanticheart.monitor.system.notification.debugNotification
import com.quanticheart.monitor.system.project.sendDataCollected
import com.quanticheart.monitor.system.project.system.uuid

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