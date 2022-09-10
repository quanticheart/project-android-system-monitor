package com.quanticheart.monitor.system.dns

import android.util.Log
import java.net.InetAddress

private val TAG = "DnsHelper"
internal fun mobDNS(host: String?): String {
    val list: MutableList<String> = ArrayList()
    var addresses = arrayOfNulls<InetAddress>(0)
    try {
        addresses = InetAddress.getAllByName(host)
    } catch (e: Exception) {
        Log.i(TAG, e.toString())
    }
    for (inetAddress in addresses) {
        list.add(inetAddress!!.hostAddress)
    }
    return list.toString()
}