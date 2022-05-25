package com.quanticheart.broadcasts

import android.content.Intent
import com.quanticheart.monitor.system.alarms.AlarmTypes
import com.quanticheart.monitor.system.alarms.CustonReceiver
import com.quanticheart.monitor.system.extentions.log

class OtherCustonReceiver : CustonReceiver() {
    override val actionKey: String
        get() = "OtherCustonReceiver"

    override val requestCode: Int
        get() = 300

    override fun type(): Pair<AlarmTypes, Int> = Pair(AlarmTypes.MINUTE, 10)

    override fun callback(intent: Intent) {
        log("ALARM", "OtherCustonReceiver")
    }

    override fun receiver() = this::class.java
}