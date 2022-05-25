package com.quanticheart.monitor.system.system.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.quanticheart.monitor.system.system.bluetooth.Bluetooth.Device

private val TAG = "Bluetooth"

@SuppressLint("MissingPermission")
internal fun Context.getBluetoothDetails(): Bluetooth {
    val bluetoothBean = Bluetooth()
    try {
        bluetoothBean.bluetoothAddress =
            Settings.Secure.getString(contentResolver, "bluetooth_address")
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            ?: return bluetoothBean
        bluetoothBean.isEnabled = bluetoothAdapter.isEnabled
        bluetoothBean.phoneName = bluetoothAdapter.name
        val pairedDevices = bluetoothAdapter.bondedDevices
        val list: MutableList<Device> = ArrayList()
        if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                val deviceBean = Device()
                deviceBean.address = device.address
                deviceBean.name = device.name
                list.add(deviceBean)
            }
        }
        bluetoothBean.device = list
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
    return bluetoothBean
}