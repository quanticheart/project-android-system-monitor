package com.quanticheart.monitor.system.uniqueid

import android.os.Build
import android.util.Log
import java.util.*

// than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
// returns 'null', then simply the ID returned will be solely based
// off their Android device information. This is where the collisions
// can happen.
// Thanks http://www.pocketmagic.net/?p=1662!
// Try not to use DISPLAY, HOST or ID - these items could change.
// If there are collisions, there will be overlapping data

// Thanks to @Roman SL!
// http://stackoverflow.com/a/4789483/950427
// Only devices with API >= 9 have android.os.Build.SERIAL
// http://developer.android.com/reference/android/os/Build.html#SERIAL
// If a user upgrades software or roots their phone, there will be a duplicate entry
// Thanks @Joe!
// http://stackoverflow.com/a/2853253/950427
// Finally, combine the values we have found by using the UUID class to create a unique identifier
internal val uniqueID: String
    get() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        val mSzDevIDShort = ("35" +
                Build.BOARD.length % 10
                + Build.BRAND.length % 10
                + Build.CPU_ABI.length % 10
                + Build.DEVICE.length % 10
                + Build.MANUFACTURER.length % 10
                + Build.MODEL.length % 10
                + Build.PRODUCT.length % 10)

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        var serial = "serial"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                serial = Build.getSerial()
            } catch (e: Exception) {
            }
        } else {
            serial = Build.SERIAL
        }
        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return UUID(mSzDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

val uniqueID2: String
    get() {
        val uniquePseudoID =
            "35" + Build.BOARD.length % 10 +
                    Build.BRAND.length % 10 +
                    Build.DEVICE.length % 10 +
                    Build.DISPLAY.length % 10 +
                    Build.HOST.length % 10 +
                    Build.ID.length % 10 +
                    Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10 +
                    Build.TAGS.length % 10 +
                    Build.TYPE.length % 10 +
                    Build.USER.length % 10
        val serial = Build.getRadioVersion()
        val uuid = UUID(uniquePseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
        Log.e("UUID", "Device ID $uuid")
        return uuid
    }