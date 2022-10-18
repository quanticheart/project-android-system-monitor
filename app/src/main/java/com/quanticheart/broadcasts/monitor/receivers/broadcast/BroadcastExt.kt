package com.quanticheart.sendcustonaction.monitor.receivers.broadcast

import android.content.Context
import android.content.Intent
import android.util.Log
import com.quanticheart.sendcustonaction.monitor.receivers.verification.isAppForeground

//
// Created by Jonn Alves on 18/10/22.
//
internal fun Context.sendAppTaskStatusBroadcast() {
    val intentSend = Intent("application.com.TASK_RUNNING")
    intentSend.putExtra("running", isAppForeground())
    intentSend.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
    sendBroadcast(intentSend)
    Log.e("APP", "Send Task Status")
}