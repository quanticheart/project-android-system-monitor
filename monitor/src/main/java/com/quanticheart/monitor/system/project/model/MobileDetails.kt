package com.quanticheart.monitor.system.project.model

import com.quanticheart.monitor.system.system.app.Package
import com.quanticheart.monitor.system.system.applist.ListAppBean
import com.quanticheart.monitor.system.system.audio.Audio
import com.quanticheart.monitor.system.system.band.Band
import com.quanticheart.monitor.system.system.battery.Battery
import com.quanticheart.monitor.system.system.bluetooth.Bluetooth
import com.quanticheart.monitor.system.system.camera.Camera
import com.quanticheart.monitor.system.system.cpu.Cpu
import com.quanticheart.monitor.system.system.emulator.Emulator
import com.quanticheart.monitor.system.system.hook.Hook
import com.quanticheart.monitor.system.system.local.Local
import com.quanticheart.monitor.system.system.memory.Memory
import com.quanticheart.monitor.system.system.moreopen.MoreOpen
import com.quanticheart.monitor.system.system.network.Network
import com.quanticheart.monitor.system.system.sdcard.SDCard
import com.quanticheart.monitor.system.system.signal.Signal
import com.quanticheart.monitor.system.system.simcard.SimCard
import com.quanticheart.monitor.system.system.wifi.Wifi

class MobileDetails {
    var details: Wifi? = null
    var pkg: Package? = null
    var appsList: List<ListAppBean>? = null
    var audio: Audio? = null
    var band: Band? = null
    var battery: Battery? = null
    var bluetooth: Bluetooth? = null
    var build: com.quanticheart.monitor.system.system.build.Build? = null
    var camera: Camera? = null
    var cpu: Cpu? = null
    var debug: com.quanticheart.monitor.system.system.debug.Debug ? = null
    var emulator: Emulator? = null
    var local: Local? = null
    var memory: Memory? = null
    var network: Network? = null
    var root: Boolean? = null
    var sdCard: SDCard? = null
    var settings: com.quanticheart.monitor.system.system.setting.Settings? = null
    var net: Signal? = null
    var sim: SimCard? = null
    var uniqueID: String? = null
    var userAgent: String? = null
    var hookExposed: Hook? = null
    var moreOpen: MoreOpen? = null
}