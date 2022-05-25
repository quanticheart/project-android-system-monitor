package com.quanticheart.monitor.system.system.screen.notch


interface ResultListener {
    fun onResult(
        isWindowNotch: Boolean,
        windowNotchHeight: Int
    )
}