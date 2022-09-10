package com.quanticheart.monitor.extentions

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String?.isValidMacAddress(): Boolean {
    // Regex to check valid
    // MAC address
    val regex = ("^([0-9A-Fa-f]{2}[:-])"
            + "{5}([0-9A-Fa-f]{2})|"
            + "([0-9a-fA-F]{4}\\."
            + "[0-9a-fA-F]{4}\\."
            + "[0-9a-fA-F]{4})$")

    // Compile the ReGex
    val p: Pattern = Pattern.compile(regex)

    // If the string is empty
    // return false
    if (this == null) {
        return false
    }

    // Find match between given string
    // and regular expression
    // uSing Pattern.matcher()
    val m: Matcher = p.matcher(this)

    // Return if the string
    // matched the ReGex
    return m.matches()
}