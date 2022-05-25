package com.quanticheart.monitor.system.project.system

import android.content.Context
import com.quanticheart.monitor.system.extentions.powerManager

fun Context.getScreenStatus() = if (powerManager.isInteractive) "on" else "off"
