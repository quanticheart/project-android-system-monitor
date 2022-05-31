package com.quanticheart.monitor.system.sharedPreferences

import android.content.Context
import com.quanticheart.monitor.system.project.model.SimpleMobileDetails

val Context.preferences: ProjectSharedPreferences
    get() = ProjectSharedPreferences(this)

fun Context.preferences(name: String): ProjectSharedPreferences {
    return ProjectSharedPreferences(this, name)
}

private const val detailsListKey = "details"
private const val detailsListKeyTmp = "detailsTMP"

fun Context.insertSimpleDetails(details: SimpleMobileDetails) {
    preferences.getModel<ArrayList<SimpleMobileDetails>?>(detailsListKey)?.let {
        it.add(details)
        preferences.putModel(detailsListKey, it)
    } ?: run {
        val list = ArrayList<SimpleMobileDetails>()
        list.add(details)
        preferences.putModel(detailsListKey, list)
    }
}

fun Context.insertTMPSimpleDetails(details: ArrayList<SimpleMobileDetails>) {
    preferences.getModel<ArrayList<SimpleMobileDetails>?>(detailsListKeyTmp)?.let {
        it.addAll(details)
        preferences.putModel(detailsListKeyTmp, it)
    } ?: run {
        preferences.putModel(detailsListKeyTmp, details)
    }
}

fun Context.sendList() {
    preferences.getModel<ArrayList<SimpleMobileDetails>?>(detailsListKey)?.let {
        val send = fakeSend(it)
        if (send) {
            preferences.delete(detailsListKey)
            preferences.delete(detailsListKeyTmp)
        } else {
            insertTMPSimpleDetails(it)
            preferences.delete(detailsListKey)
        }
    }
}


fun fakeSend(arrayList: ArrayList<SimpleMobileDetails>): Boolean {
    return true
}