package com.quanticheart.sendcustonaction.monitor

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

//
// Created by Jonn Alves on 17/10/22.
//
class ActivityLifeCycleObserver(
    private val lifecycle: Lifecycle,
    private val callback: (String) -> Unit
) : DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        callback.invoke("FOREGROUND")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        callback.invoke("BACKGROUND")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        callback.invoke("BACKGROUND - DESTROY")
        lifecycle.removeObserver(this)
    }
}

fun Application.getLifeCycle() = ProcessLifecycleOwner.get().lifecycle