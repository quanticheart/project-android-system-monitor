package com.quanticheart.monitor.system.system.root

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


private val TAG = "RootHelper"
internal fun Context.getMobileRootDetails(): Boolean {
    return existingRWPaths().isNotEmpty() || existingRootPackages(this).isNotEmpty() || existingDangerousProperties().isNotEmpty() || existingRootFiles().isNotEmpty()
}

private val SU_PATHS = arrayOf(
    "/system/app/Superuser.apk",
    "/sbin/su",
    "/system/bin/su",
    "/system/xbin/su",
    "/data/local/xbin/su",
    "/data/local/bin/su",
    "/system/sd/xbin/su",
    "/system/bin/failsafe/su",
    "/data/local/su",
    "/su/bin/su"
)
private val KNOWN_ROOT_APPS_PACKAGES = arrayOf(
    "com.noshufou.android.su",
    "com.noshufou.android.su.elite",
    "eu.chainfire.supersu",
    "com.koushikdutta.superuser",
    "com.thirdparty.superuser",
    "com.yellowes.su"
)
private val KNOWN_DANGEROUS_APPS_PACKAGES = arrayOf(
    "com.koushikdutta.rommanager",
    "com.dimonvideo.luckypatcher",
    "com.chelpus.lackypatch",
    "com.ramdroid.appquarantine"
)
private val KNOWN_ROOT_CLOAKING_PACKAGES = arrayOf(
    "com.devadvance.rootcloak",
    "de.robv.android.xposed.installer",
    "com.saurik.substrate",
    "com.devadvance.rootcloakplus",
    "com.zachspong.temprootremovejb",
    "com.amphoras.hidemyroot",
    "com.formyhm.hideroot"
)
private val PATHS_THAT_SHOULD_NOT_BE_WRITABLE = arrayOf(
    "/system",
    "/system/bin",
    "/system/sbin",
    "/system/xbin",
    "/vendor/bin",
    "/sbin",
    "/etc"
)
private val DANGEROUS_PROPERTIES: Map<String, String> = HashMap()
private fun existingRootFiles(): List<String> {
    val filesFound: MutableList<String> = ArrayList()
    for (path in SU_PATHS) {
        if (File(path).exists()) {
            filesFound.add(path)
        }
    }
    return filesFound
}

/**
 * Checks for packages that are known to indicate root.
 *
 * @return - list of such packages found
 */
private fun existingRootPackages(context: Context): List<String> {
    val packages = ArrayList<String>()
    packages.addAll(listOf(*KNOWN_ROOT_APPS_PACKAGES))
    packages.addAll(listOf(*KNOWN_DANGEROUS_APPS_PACKAGES))
    packages.addAll(listOf(*KNOWN_ROOT_CLOAKING_PACKAGES))
    val pm: PackageManager = context.packageManager
    val packagesFound: MutableList<String> = ArrayList()
    for (packageName in packages) {
        try {
            // Root app detected
            pm.getPackageInfo(packageName, 0)
            packagesFound.add(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i(TAG, e.toString())
        }
    }
    return packagesFound
}

/**
 * Checks system properties for any dangerous properties that indicate root.
 *
 * @return - list of dangerous properties that indicate root
 */
private fun existingDangerousProperties(): List<String> {
    DANGEROUS_PROPERTIES.plus("[ro.debuggable]" to "[1]")
    DANGEROUS_PROPERTIES.plus("[ro.secure]" to "[0]")
    val lines = propertiesReader()
    val propertiesFound: MutableList<String> = ArrayList()
    assert(lines != null)
    for (line in lines!!) {
        for (key in DANGEROUS_PROPERTIES.keys) {
            if (line.contains(key) && line.contains(DANGEROUS_PROPERTIES[key]!!)) {
                propertiesFound.add(line)
            }
        }
    }
    return propertiesFound
}

/**
 * When you're root you can change the write permissions on common system directories.
 * This method checks if any of the paths in PATHS_THAT_SHOULD_NOT_BE_WRITABLE are writable.
 *
 * @return all paths that are writable
 */
private fun existingRWPaths(): List<String> {
    val lines = mountReader()
    val pathsFound: MutableList<String> = ArrayList()
    assert(lines != null)
    for (line in lines!!) {
        // Split lines into parts
        val args = line.split(" ").toTypedArray()
        if (args.size < 4) {
            // If we don't have enough options per line, skip this and log an error
            continue
        }
        val mountPoint = args[1]
        val mountOptions = args[3]
        for (pathToCheck in PATHS_THAT_SHOULD_NOT_BE_WRITABLE) {
            if (mountPoint.equals(pathToCheck, ignoreCase = true)) {
                // Split options out and compare against "rw" to avoid false positives
                for (option in mountOptions.split(",").toTypedArray()) {
                    if ("rw".equals(option, ignoreCase = true)) {
                        pathsFound.add(pathToCheck)
                        break
                    }
                }
            }
        }
    }
    return pathsFound
}

/**
 * Used for existingDangerousProperties().
 *
 * @return - list of system properties
 */
private fun propertiesReader(): Array<String>? {
    var inputstream: InputStream? = null
    try {
        inputstream = Runtime.getRuntime().exec("getprop").inputStream
    } catch (e: IOException) {
        Log.i(TAG, e.toString())
    }
    if (inputstream == null) {
        return null
    }
    var allProperties = ""
    try {
        allProperties = Scanner(inputstream).useDelimiter("\\A").next()
    } catch (e: NoSuchElementException) {
        Log.i(TAG, e.toString())
    }
    return allProperties.split("\n").toTypedArray()
}

/**
 * Used for existingRWPaths().
 *
 * @return - list of directories and their properties
 */
private fun mountReader(): Array<String>? {
    var inputstream: InputStream? = null
    try {
        inputstream = Runtime.getRuntime().exec("mount").inputStream
    } catch (e: IOException) {
        Log.i(TAG, e.toString())
    }
    if (inputstream == null) {
        return null
    }
    var allPaths = ""
    try {
        allPaths = Scanner(inputstream).useDelimiter("\\A").next()
    } catch (e: NoSuchElementException) {
        Log.i(TAG, e.toString())
    }
    return allPaths.split("\n").toTypedArray()
}