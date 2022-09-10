package com.quanticheart.monitor.project.system

import android.content.Context
import com.quanticheart.monitor.extentions.powerManager

fun Context.getScreenStatus() = if (powerManager.isInteractive) "on" else "off"
