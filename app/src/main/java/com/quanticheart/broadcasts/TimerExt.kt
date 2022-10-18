package com.quanticheart.broadcasts

import android.os.Handler
import java.util.*

//
// Created by Jonn Alves on 18/10/22.
//
private var timerSys: Timer? = null
private val timerHandler = Handler()

private fun stopTimer() {
    timerSys?.apply {
        cancel()
        purge()
    }
}

fun startTimerTest(callback: () -> Unit) {
    timerSys = Timer()
    timerSys?.schedule(object : TimerTask() {
        override fun run() {
            timerHandler.post {
                callback()
            }
        }
    }, 0, 5000)
}