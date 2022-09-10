package com.quanticheart.broadcasts.sendMacAddress.response

import com.google.gson.annotations.SerializedName

data class HardwareRegisterResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("code")
    val code: Int
)