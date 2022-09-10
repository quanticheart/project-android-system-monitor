package com.quanticheart.broadcasts.sendMacAddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quanticheart.broadcasts.application.AppApplication
import com.quanticheart.broadcasts.extentions.insertMacAndToken
import com.quanticheart.broadcasts.sendMacAddress.data.InsertMacRepository

class InsertMacViewModel : ViewModel() {

    private val repository = InsertMacRepository(AppApplication.appContext)
    val insertSuccess = MutableLiveData<Boolean>().apply { value = false }
    val insertError = MutableLiveData<Boolean>().apply { value = false }
    var errorMsg = ""

    fun insertMacAddress(uuid: String, macAddress: String) {
        repository.sendMacAddressToServer(uuid, macAddress) { status, msg ->
            if (status) {
                AppApplication.appContext.insertMacAndToken(uuid, macAddress)
                insertSuccess.value = true
            } else {
                msg?.let {
                    insertError.value = true
                    insertError.value = false
                    errorMsg = it
                } ?: run {
                    insertSuccess.value = false
                }
            }
        }
    }
}
