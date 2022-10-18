package com.quanticheart.sendcustonaction.monitor.receivers.verification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.quanticheart.sendcustonaction.monitor.receivers.broadcast.sendAppTaskStatusBroadcast

//
// Created by Jonn Alves on 18/10/22.
//
class AppVerification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getBooleanExtra("app_validation", false)) {
            context.sendAppTaskStatusBroadcast()
        }
    }
}