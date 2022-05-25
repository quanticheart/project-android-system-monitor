package com.quanticheart.monitor.system.system.hook

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Process
import android.text.TextUtils
import android.util.Log
import com.quanticheart.monitor.system.system.others.CommandUtil
import com.quanticheart.monitor.system.system.others.MobileNativeHelper
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Modifier

private val TAG = "HookInfo"

/**
 * 判断是否有xposed等hook工具
 *
 * @return
 */
internal fun Context.getXposedHookDetails(): Hook {
    val hookBean = Hook()
    val xposedBean = Hook.Xposed()
    val substrateBean = Hook.Substrate()
    val fridaBean = Hook.Frida()
    try {
        chargeXposedPackage(this, xposedBean, substrateBean)
        chargeXposedHookMethod(xposedBean, substrateBean)
        chargeXposedJars(xposedBean, substrateBean, fridaBean)
        fridaBean.isCheckRunningProcesses = (checkRunningProcesses(this))
        addMethod(xposedBean)
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    substrateBean.setcSo(MobileNativeHelper.checkSubstrateBySo() == 1)
    val mapData = MobileNativeHelper.checkHookByMap()
    val packageData = MobileNativeHelper.checkHookByPackage()
    if (!TextUtils.isEmpty(mapData)) {
        if (mapData?.contains("xposed") == true) {
            xposedBean.setcMap(true)
        }
        if (mapData?.contains("frida") == true) {
            fridaBean.setcMap(true)
        }
        if (mapData?.contains("substrate") == true) {
            substrateBean.setcMap(true)
        }
    }
    if (!TextUtils.isEmpty(packageData)) {
        if (packageData?.contains("xposed") == true) {
            xposedBean.setcPackage(true)
        }
        if (packageData?.contains("substrate") == true) {
            substrateBean.setcPackage(true)
        }
    }
    hookBean.isHaveXposed = xposedBean
    hookBean.isHaveSubstrate = substrateBean
    hookBean.isHaveFrida = fridaBean
    return hookBean
}

private fun checkRunningProcesses(context: Context): Boolean {
    var returnValue = false
    // Get currently running application processes
    val activityManager: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val list: List<ActivityManager.RunningServiceInfo> =
        activityManager.getRunningServices(300)
    var tempName: String
    for (i in list.indices) {
        tempName = list[i].process
        if (tempName.contains("fridaserver")) {
            returnValue = true
        }
    }
    return returnValue
}

/**
 * 新增方法
 *
 * 是否安装了Xposed
 */
private fun addMethod(xposed: Hook.Xposed) {
    xposed.isCheckClassLoader = (testClassLoader())
    xposed.isCheckNativeMethod = (checkNativeMethod())
    xposed.isCheckSystem = (checkSystem())
    xposed.isCheckExecLib = (checkExecLib())
    xposed.isCheckXposedBridge = (checkXposedBridge())
}

/**
 * 新增检测载入Xposed工具类
 * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
 *
 * @return 是否安装了Xposed
 */
private fun testClassLoader(): Boolean {
    try {
        ClassLoader.getSystemClassLoader()
            .loadClass("de.robv.android.xposed.XposedHelpers")
        return true
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    }
    return false
}

/**
 * 新增判断系统方法调用钩子
 * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
 *
 * @return 是否安装了Xposed
 */
private fun checkNativeMethod(): Boolean {
    try {
        val method = Throwable::class.java.getDeclaredMethod("getStackTrace")
        return Modifier.isNative(method.modifiers)
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    }
    return false
}

/**
 * 新增虚拟检测Xposed环境
 * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
 *
 * @return 是否安装了Xposed
 */
private fun checkSystem(): Boolean {
    return System.getProperty("vxp") != null
}

/**
 * 寻找Xposed运行库文件
 * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
 *
 * @return 是否安装了Xposed
 */
private fun checkExecLib(): Boolean {
    val result = CommandUtil().exec("ls /system/lib")
    return if (TextUtils.isEmpty(result)) {
        false
    } else result?.contains("xposed") ?: false
}

/**
 * 环境变量特征字判断
 * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
 *
 * @return 是否安装了Xposed
 */
private fun checkXposedBridge(): Boolean {
    val result = System.getenv("CLASSPATH")
    if (result != null) {
        return if (TextUtils.isEmpty(result)) {
            false
        } else result.contains("XposedBridge")
    }
    return false
}

/**
 * 检查包名是否存在
 *
 * @param context
 * @return
 */
private fun chargeXposedPackage(
    context: Context,
    xposed: Hook.Xposed,
    substrate: Hook.Substrate
) {
    val packageManager: PackageManager = context.applicationContext.packageManager
    val appliacationInfoList: List<ApplicationInfo> =
        packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            ?: return
    for (item in appliacationInfoList) {
        //新增包名检测 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
        if ("de.robv.android.xposed.installer" == item.packageName || "io.va.exposed" == item.packageName) {
            xposed.isCheckXposedPackage = (true)
        }
        if ("com.saurik.substrate" == item.packageName) {
            substrate.isCheckSubstratePackage = (true)
        }
    }
}

/**
 * 检测调用栈中的可疑方法
 */
private fun chargeXposedHookMethod(
    xposed: Hook.Xposed,
    substrate: Hook.Substrate
) {
    try {
        throw Exception("Deteck hook")
    } catch (e: Exception) {
        var zygoteInitCallCount = 0
        for (item in e.stackTrace) {
            // 检测"com.android.internal.os.ZygoteInit"是否出现两次，如果出现两次，则表明Substrate框架已经安装
            if ("com.android.internal.os.ZygoteInit" == item.className) {
                zygoteInitCallCount++
                if (zygoteInitCallCount == 2) {
                    substrate.isCheckSubstrateHookMethod = (true)
                }
            }
            if ("com.saurik.substrate.MS$2" == item.className && "invoke" == item.methodName) {
                substrate.isCheckSubstrateHookMethod = (true)
            }
            if ("de.robv.android.xposed.XposedBridge" == item.className && "main" == item.methodName) {
                xposed.isCheckXposedHookMethod = (true)
            }
            if ("de.robv.android.xposed.XposedBridge" == item.className && "handleHookedMethod" == item.methodName) {
                xposed.isCheckXposedHookMethod = (true)
            }
        }
    }
}

/**
 * 检测内存中可疑的jars
 */
private fun chargeXposedJars(
    xposed: Hook.Xposed,
    substrate: Hook.Substrate,
    frida: Hook.Frida
) {
    val libraries: MutableSet<String> = HashSet()
    val mapsFilename = "/proc/" + Process.myPid() + "/maps"
    try {
        val reader = BufferedReader(FileReader(mapsFilename))
        var line: String
        while (reader.readLine().also { line = it } != null) {
            if (line.lowercase().contains("frida")) {
                frida.isCheckFridaJars = (true)
            }
            if (line.endsWith(".so") || line.endsWith(".jar")) {
                val n = line.lastIndexOf(" ")
                libraries.add(line.substring(n + 1))
            }
        }
        for (library in libraries) {
            if (library.contains("com.saurik.substrate")) {
                substrate.isCheckSubstrateJars = (true)
            }
            if (library.contains("XposedBridge.jar")) {
                xposed.isCheckXposedJars = (true)
            }
        }
        reader.close()
    } catch (e: Exception) {
    }
}
