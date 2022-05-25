package com.quanticheart.monitor.system.project.system

import java.text.SimpleDateFormat
import java.util.*

private fun createDate(pattern: String, date: Date): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
}

fun now(pattern: String? = null): String =
    createDate(pattern ?: "yyyy/MM/dd HH:mm:ss", Date())

fun Long.toDate() = createDate("yyyy/MM/dd HH:mm:ss", Date(this))
