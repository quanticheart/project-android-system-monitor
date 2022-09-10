package com.quanticheart.broadcasts.sendMacAddress.data

import android.content.Context
import com.quanticheart.broadcasts.extentions.getMacAndToken
import com.quanticheart.broadcasts.sendMacAddress.request.HardwarePingRegisterRequest
import com.quanticheart.broadcasts.sendMacAddress.request.HardwareRegisterRequest
import com.quanticheart.broadcasts.sendMacAddress.response.HardwareRegisterResponse
import com.quanticheart.monitor.system.getConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InsertMacRepository(private val context: Context) {

    private val endpoints = context.getConnection(
        "https://api.quanticheart.com/",
        mutableMapOf<String, String>().apply {
            this["api-token"] = ""
        }).create(InsertMacEndpoints::class.java)

    fun sendMacAddressToServer(
        token: String,
        macAddress: String,
        callback: (Boolean, String?) -> Unit
    ) {
        endpoints.sendRegister(HardwareRegisterRequest(token, macAddress))
            .enqueue(object : Callback<HardwareRegisterResponse> {
                override fun onResponse(
                    call: Call<HardwareRegisterResponse>,
                    response: Response<HardwareRegisterResponse>
                ) {
                    val responseStatus = response.body()?.status == true
                    if (responseStatus) {
                        callback(true, null)
                    } else {
                        if (response.body()?.code == 75) {
                            callback(false, response.body()?.msg ?: "Error")
                        } else {
                            callback(false, null)
                        }
                    }
                }

                override fun onFailure(call: Call<HardwareRegisterResponse>, t: Throwable) {
                    callback(false, t.message)
                }
            })
    }

    fun sendMobilePingToServer(
        data: String,
        callback: (Boolean) -> Unit
    ) {
        val sessionHardware = context.getMacAndToken()
        val token: String = sessionHardware.second
        val macAddress: String = sessionHardware.first
        endpoints.sendMobilePing(HardwarePingRegisterRequest(token, macAddress, data))
            .enqueue(object : Callback<HardwareRegisterResponse> {
                override fun onResponse(
                    call: Call<HardwareRegisterResponse>,
                    response: Response<HardwareRegisterResponse>
                ) {
                    val responseStatus = response.body()?.status == true
                    if (responseStatus) {
                        callback(true)
                    } else {
                        if (response.body()?.code == 75) {
                            callback(false)
                        } else {
                            callback(false)
                        }
                    }
                }

                override fun onFailure(call: Call<HardwareRegisterResponse>, t: Throwable) {
                    callback(false)
                }
            })
    }
}
