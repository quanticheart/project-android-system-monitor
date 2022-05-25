package com.quanticheart.monitor.system.system

import android.content.Context
import com.quanticheart.monitor.system.extentions.powerManager

fun Context.systemScreenStatus() = powerManager.isInteractive