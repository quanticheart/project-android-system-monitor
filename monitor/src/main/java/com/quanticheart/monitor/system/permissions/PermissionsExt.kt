package com.quanticheart.monitor.system.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Permissions
 *
 * Set permissions for project
 */
private val permissions: Array<String>
    get() = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

/**
 * Get background location permission
 * READ = https://stackoverflow.com/questions/66475027/activityresultlauncher-with-requestmultiplepermissions-contract-doesnt-show-per
 */
private val backgroundLocationPermission: String
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = Manifest.permission.ACCESS_BACKGROUND_LOCATION

fun ComponentActivity.requestPermissions(callback: (allOK: Boolean) -> Unit) {
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        callback(verifyPermissions(permissions))
    }
    // Before you perform the actual permission request, check whether your app
    // already has the permissions, and whether your app needs to show a permission
    // rationale dialog. For more details, see Request permissions.
    val list = getPermissionsList()
    if (list.isNotEmpty()) {
        locationPermissionRequest.launch(list)
    } else {
        callback(true)
    }
}

private fun verifyRequestBackgroundLocationPermissions(callback: (Boolean) -> Unit) {
    val hasPreciseLocation = permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)
    val hasApproximateLocation = permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)
    if (hasPreciseLocation && hasApproximateLocation) {
        callback(true)
    }
}

fun ComponentActivity.verifyBackGroundLocationPermission(callback: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        verifyRequestBackgroundLocationPermissions {
            if (!checkPermission(backgroundLocationPermission)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    showMsg(packageManager.backgroundPermissionOptionLabel.toString()) {
                        callback()
                    }
                } else {
                    showMsg("I Need background locations permission") {
                        callback()
                    }
                }
            }
        }
    }
}

fun ComponentActivity.requestBackGroundLocationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        requestPermission(backgroundLocationPermission)
    }
}

fun ComponentActivity.requestPermission(permission: String) {
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            checkRationale(permission)
        }
    }
    // Before you perform the actual permission request, check whether your app
    // already has the permissions, and whether your app needs to show a permission
    // rationale dialog. For more details, see Request permissions.
    locationPermissionRequest.launch(permission)
}

private fun ComponentActivity.verifyPermissions(permissions: MutableMap<String, Boolean>): Boolean {
    val verify = arrayListOf<Boolean>()
    getPermissionsList().forEach { permission ->
        verify.add(permissions.getOrDefault(permission, false))
    }
    val allOK = verify.allTrue()
    if (!allOK)
        checkRationale(permissions)

    return allOK
}

private fun Context.checkPermissions(): Array<String> {
    val listPermissionsRequired = ArrayList<String>()
    permissions.forEach { manifestPermission ->
        if (!checkPermission(manifestPermission)) {
            listPermissionsRequired.add(manifestPermission)
        }
    }
    return listPermissionsRequired.toTypedArray()
}

private fun Context.checkPermission(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

private fun Context.getPermissionsList() = checkPermissions()

/**
 * Open App Settings
 */
private fun Activity.goToSettings() {
    this.startActivity(createIntentSettings(this))
}

private fun List<Boolean>.allTrue() = !contains(false)

private fun createIntentSettings(activity: Activity): Intent {
    val myAppSettings = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:" + activity.packageName)
    )
    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
    myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    return myAppSettings
}

private fun ComponentActivity.checkRationale(permission: String) {
    var rationale = false
    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
        if (!checkPermission(permission)) {
            rationale = true
        }
    }
    if (rationale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            showMsg(packageManager.backgroundPermissionOptionLabel as String)
        } else {
            showMsg("Have One Permission")
        }
}

private fun ComponentActivity.checkRationale(permissions: MutableMap<String, Boolean>) {
    val permissionList = ArrayList<String>()
    for (permission in permissions) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission.key)) {
            if (checkPermission(permission.key)) {
                permissionList.add(permission.key)
            }
        }
    }
    if (permissionList.isNotEmpty()) {
        when (permissionList.size) {
            1 -> showMsg("Have One Permission")
            else -> showMsg("Have Multiple Permissions")
        }
    }
}

private fun ComponentActivity.showMsg(msg: String, callbackOK: (() -> Unit)? = null) {
    val alertDialogBuilder = AlertDialog.Builder(this)
    val alertDialogRational = alertDialogBuilder.setTitle("Permissions Required")
        .setMessage(msg)
        .setPositiveButton("Settings") { dialog, _ ->
            dialog.dismiss()
            callbackOK?.let { it() } ?: run {
                goToSettings()
            }
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .setCancelable(false)
        .create()
    alertDialogRational.show()
}