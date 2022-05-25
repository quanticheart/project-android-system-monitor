package com.quanticheart.broadcasts

import android.app.Application
import com.quanticheart.core.system.receivers.registerBootReceiver
import com.quanticheart.monitor.system.alarms.AlarmActionListeners
import com.quanticheart.monitor.system.alarms.configAlarmActions
import com.quanticheart.monitor.system.extentions.log
import com.quanticheart.monitor.system.notification.debugNotification
import com.quanticheart.monitor.system.project.collectData
import com.quanticheart.monitor.system.project.sendDataCollected

class AppApplication : Application(), AlarmActionListeners {

    override fun onCreate() {
        super.onCreate()
        configAlarmActions(this)
        registerBootReceiver {
            debugNotification("Smartphone boot finish")
        }
        OtherCustonReceiver().register(this)
    }

    override fun receiveMinuteAction() {
        log("ALARM", "collectData")
        collectData()
    }

    override fun receiveHourAction() {
        debugNotification("receiveHourAction")
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