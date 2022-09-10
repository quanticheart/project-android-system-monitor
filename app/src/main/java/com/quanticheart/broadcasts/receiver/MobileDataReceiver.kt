package com.quanticheart.broadcasts.receiver

import android.content.Context
import android.content.Intent
import com.quanticheart.broadcasts.extentions.collectData
import com.quanticheart.monitor.alarms.AlarmTypes
import com.quanticheart.monitor.alarms.CustonReceiver
import com.quanticheart.monitor.extentions.log

class MobileDataReceiver : CustonReceiver() {
    override val actionKey: String
        get() = "mobileDataCollect"

    override val requestCode: Int
        get() = 300

    override fun type(): Pair<AlarmTypes, Int> = Pair(AlarmTypes.MINUTE, 1)

    override fun callback(context: Context, intent: Intent) {
        log("ALARM", "collectData in MobileDataReceiver")
        context.collectData()
    }

    override fun receiver() = this::class.java
}