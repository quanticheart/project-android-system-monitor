package com.quanticheart.broadcasts.sendMacAddress.data

import com.quanticheart.broadcasts.sendMacAddress.request.HardwarePingRegisterRequest
import com.quanticheart.broadcasts.sendMacAddress.request.HardwareRegisterRequest
import com.quanticheart.broadcasts.sendMacAddress.response.HardwareRegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface InsertMacEndpoints {
    @POST("/mobile/android/register")
    fun sendRegister(@Body request: HardwareRegisterRequest): Call<HardwareRegisterResponse>

    @POST("/mobile/android/data")
    fun sendMobilePing(@Body request: HardwarePingRegisterRequest): Call<HardwareRegisterResponse>
}