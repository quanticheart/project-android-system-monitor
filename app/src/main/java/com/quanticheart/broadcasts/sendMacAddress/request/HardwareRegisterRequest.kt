package com.quanticheart.broadcasts.sendMacAddress.request

import com.google.gson.annotations.SerializedName

data class HardwareRegisterRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("mac_address")
    val mac_address: String
)