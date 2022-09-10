package com.quanticheart.monitor.system.others

object MobileNativeHelper : MobileInterface {
    external fun bootIdC(): String?
    external fun entropyAvailC(): String?
    external fun poolSizeC(): String?
    external fun readWakeupThresholdC(): String?
    external fun writeWakeupThresholdC(): String?
    external fun uuidC(): String?
    external fun uRandomMinReseedSecsC(): String?
    external fun kennel(): String?
    external fun checkMoreOpenByUid(): Int
    external fun checkSubstrateBySo(): Int
    external fun checkHookByMap(): String?
    external fun checkHookByPackage(): String?

    init {
        System.loadLibrary("fairymob-lib")
    }
}