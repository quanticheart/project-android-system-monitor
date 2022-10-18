package com.quanticheart.monitor.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager

private var broadcastManager: LocalBroadcastManager? = null
private var reloadReceiver: BroadcastReceiver? = null

@Suppress("unused")
fun Fragment.destroyBroadcastManager() {
    reloadReceiver?.let {
        broadcastManager?.unregisterReceiver(it)
        reloadReceiver = null
    }
}

fun Fragment.createBroadcastManager(vararg actions: String, callback: (action: String) -> Unit) {
    val filter = IntentFilter().apply {
        actions.forEach {
            addAction(it)
        }
    }

    reloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.action?.let { callback(it) }
        }
    }

    context?.let { context ->
        LocalBroadcastManager.getInstance(context).apply {
            broadcastManager = this
            reloadReceiver?.let { this.registerReceiver(it, filter) }
        }
    }
}

fun Fragment?.sendBroadcastAction(action: String, extras: Bundle? = null) {
    this?.context?.sendBroadcastAction(action, extras)
}

fun Context?.sendBroadcastAction(action: String, extras: Bundle? = null) {
    val intent = Intent(action).apply { extras?.let { putExtras(it) } }
    this?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent) }
}
