package com.quanticheart.monitor.system.screen.notch


interface ResultListener {
    fun onResult(
        isWindowNotch: Boolean,
        windowNotchHeight: Int
    )
}