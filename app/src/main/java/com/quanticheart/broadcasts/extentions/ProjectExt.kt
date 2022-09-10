package com.quanticheart.broadcasts.extentions

import android.content.Context
import com.quanticheart.broadcasts.sendMacAddress.data.InsertMacRepository
import com.quanticheart.monitor.extentions.toJson
import com.quanticheart.monitor.project.getSimpleDetails
import com.quanticheart.monitor.project.model.SimpleMobileDetails
import com.quanticheart.monitor.sharedPreferences.ProjectSharedPreferences
import com.quanticheart.monitor.sharedPreferences.preferences

val Context.preferencesMobileData: ProjectSharedPreferences
    get() = preferences("data_mobile")

fun Context.verifyMac(): Boolean {
    val mac = preferencesMobileData.getString("mac")
    return mac != null
}

fun Context.insertMacAndToken(token: String, mac: String): Boolean {
    preferencesMobileData.putString("mac", "$mac##$token")
    return verifyMac()
}

fun Context.updateToken(newToken: String): Boolean {
    val sessionHardware = getMacAndToken()
    val macAddress: String = sessionHardware.first
    preferencesMobileData.putString("mac", "$macAddress##$newToken")
    return verifyMac()
}

fun Context.sendDataToServer(mobileDetails: SimpleMobileDetails) {
    val repository = InsertMacRepository(this)
    repository.sendMobilePingToServer(mobileDetails.toJson()) {}
}

fun Context.collectData() {
//    insertSimpleDetails(getSimpleDetails())
    val details = getSimpleDetails()
    sendDataToServer(details)
}


fun Context.getMacAndToken(): Pair<String, String> {
    val session = preferencesMobileData.getString("mac")
    val data = session?.split("##")
    return Pair(data?.get(0) ?: "", data?.get(1) ?: "")
}
