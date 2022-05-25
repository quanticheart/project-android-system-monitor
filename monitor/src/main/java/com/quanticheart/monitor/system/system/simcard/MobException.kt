package com.quanticheart.monitor.system.system.simcard

class MobException : Exception {
    constructor(error: String?) : super(error)
    constructor(throwable: Throwable?) : super(throwable)
}