package com.quanticheart.monitor.system.useragent

import android.content.Context
import android.text.TextUtils
import android.webkit.WebSettings
import com.quanticheart.monitor.extentions.UNKNOWN_PARAM
import java.lang.reflect.Method

internal fun Context.defaultUserAgent(): String {
    var userAgent: String? = null
    try {
        val localMethod: Method = WebSettings::class.java.getDeclaredMethod(
            "getDefaultUserAgent", Context::class.java
        )
        userAgent = localMethod.invoke(
            WebSettings::class.java,
            this
        ) as String
    } catch (localException: Exception) {
    }
    return if (TextUtils.isEmpty(userAgent)) UNKNOWN_PARAM else userAgent!!
}