package com.quanticheart.broadcasts.sendMacAddress

import com.google.gson.annotations.SerializedName

data class HardwarePingRegisterRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("mac_address")
    val mac_address: String,
    @SerializedName("data")
    val data: String
)