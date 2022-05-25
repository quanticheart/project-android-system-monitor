package com.quanticheart.monitor.system.system

import android.content.Context
import android.location.LocationManager
import android.util.Log
import java.lang.Exception

private fun locationEnabled(context:Context) {
    val lm: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var gps_enabled = false
    var network_enabled = false
    try {
        if (lm != null) {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            Log.e("","Location: Gps:$gps_enabled, Network:$network_enabled")
        }
    } catch (ignored: Exception) {
    }
    if (!gps_enabled && !network_enabled) {
        // notify user
//            alertUser();
    }
}
