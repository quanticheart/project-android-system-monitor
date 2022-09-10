package com.quanticheart.broadcasts.receiver

import android.content.Context
import android.content.Intent
import com.quanticheart.monitor.alarms.AlarmTypes
import com.quanticheart.monitor.alarms.CustonReceiver
import com.quanticheart.monitor.extentions.log

class OtherCustonReceiver : CustonReceiver() {
    override val actionKey: String
        get() = "OtherCustonReceiver"

    override val requestCode: Int
        get() = 300

    override fun type(): Pair<AlarmTypes, Int> = Pair(AlarmTypes.MINUTE, 1)

    override fun callback(context: Context, intent: Intent) {
        log("ALARM", "OtherCustonReceiver")
    }

    override fun receiver() = this::class.java
}