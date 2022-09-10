package com.quanticheart.monitor.project.system

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.quanticheart.monitor.extentions.locationManager
import com.quanticheart.monitor.extentions.log
import com.quanticheart.monitor.project.model.SimpleMobileDetails
import java.util.*

private const val tag = "getLocationInfo"

internal fun Context.getGPlayLocationInfo(): SimpleMobileDetails.Location {
    val loc = SimpleMobileDetails.Location()
    val lm: LocationManager = locationManager
    try {
        val country = Locale.getDefault().country
        val language = Locale.getDefault().language

        val gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val locationEnabled = networkEnabled || gpsEnabled
        val permissions = checkPermissions()

        log(tag, "Location: Gps:$gpsEnabled, Network:$networkEnabled")
        log(tag, "Location:$country, $language")
        log(tag, "Location:$locationEnabled")
        log(tag, "Location:$permissions")

        if (locationEnabled && permissions) {
            getLastLocation()
            zoomMyCurrentLocation()
        } else {

        }
    } catch (e: Exception) {
        log(tag, e.toString())
    }
    return loc
}

@SuppressLint("MissingPermission")
private fun Context.getLastLocation() {
    LocationServices.getFusedLocationProviderClient(this).apply {
        lastLocation.addOnCompleteListener {
            val location = it.result
            if (location == null) {
                requestNewLocationData()
            } else {
                val lat = location.latitude
                val long = location.longitude
                log(tag, "addOnCompleteListener lag=$lat:long=$long")
            }
        }.addOnSuccessListener {
            val lat = it.latitude
            val long = it.longitude
            log(tag, "addOnSuccessListener lag=$lat:long=$long")
        }
    }
}

@SuppressLint("MissingPermission")
private fun FusedLocationProviderClient.requestNewLocationData() {
    val mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = 5
    mLocationRequest.fastestInterval = 0
    mLocationRequest.numUpdates = 1
    Looper.myLooper()?.let { requestLocationUpdates(mLocationRequest, mLocationCallback, it) }
}

private val mLocationCallback: LocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        val location = locationResult.lastLocation
        val lat = location.latitude
        val long = location.longitude
        log(tag, "onLocationResult lag=$lat:long=$long")
    }
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

/**
 *
 */

@SuppressLint("MissingPermission")
private fun Context.zoomMyCurrentLocation() {
    val locationManager = locationManager
    val criteria = Criteria()
    val location = locationManager.getBestProvider(criteria, false)
        ?.let { locationManager.getLastKnownLocation(it) }
    if (location != null) {
        val lat: Double = location.latitude
        val long: Double = location.longitude
        log(tag, "zoomMyCurrentLocation lag=$lat:long=$long")
    } else {
        setMyLastLocation()
    }
}

@SuppressLint("MissingPermission")
private fun Context.setMyLastLocation() {
    log(tag, "setMyLastLocation: excecute, and get last location")
    LocationServices.getFusedLocationProviderClient(this).apply {
        lastLocation.addOnSuccessListener {
            if (it != null) {
                val lat: Double = it.latitude
                val long: Double = it.longitude
                log(tag, "setMyLastLocation lag=$lat:long=$long")
            }
        }
    }
}
