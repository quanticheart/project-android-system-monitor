package com.quanticheart.monitor.system.project.system

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.quanticheart.monitor.system.extentions.locationManager
import com.quanticheart.monitor.system.extentions.log
import com.quanticheart.monitor.system.project.model.SimpleMobileDetails
import java.util.*

private const val tag = "getLocationInfo"

internal fun Context.getLocationInfo(): SimpleMobileDetails.Location {
    val loc = SimpleMobileDetails.Location()
    val lm: LocationManager = locationManager
    try {
        loc.systemCountry = Locale.getDefault().country
        loc.systemLanguage = Locale.getDefault().language

        val gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        loc.enable = networkEnabled || gpsEnabled
        loc.permissionsGranted = checkPermissions()

        if (loc.enable && loc.permissionsGranted) {
            getLastLocation(loc)
        }
    } catch (e: Exception) {
        log(tag, e.toString())
    }
    return loc
}

private fun Context.checkPermissions() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
} else {
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
private fun Context.getLastLocation(loc: SimpleMobileDetails.Location) {
    val locationManager = locationManager
    val criteria = Criteria()
    val location = locationManager.getBestProvider(criteria, false)
        ?.let { locationManager.getLastKnownLocation(it) }
    if (location != null) {
        loc.latitude = location.latitude
        loc.longitude = location.longitude
    }
}
