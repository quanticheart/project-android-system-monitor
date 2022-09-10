package com.quanticheart.monitor.system

import android.content.Context
import com.quanticheart.monitor.extentions.powerManager

fun Context.systemScreenStatus() = powerManager.isInteractive