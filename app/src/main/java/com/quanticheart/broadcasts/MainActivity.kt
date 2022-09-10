package com.quanticheart.broadcasts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.broadcasts.databinding.ActivityMainBinding
import com.quanticheart.broadcasts.extentions.getMacAndToken
import com.quanticheart.broadcasts.extentions.verifyMac
import com.quanticheart.broadcasts.sendMacAddress.InsertMacActivity
import com.quanticheart.monitor.asyncTask.TaskListener
import com.quanticheart.monitor.asyncTask.startTask
import com.quanticheart.monitor.extentions.toJson
import com.quanticheart.monitor.notification.debugNotification
import com.quanticheart.monitor.permissions.requestPermissions
import com.quanticheart.monitor.project.getInfo
import com.quanticheart.monitor.project.getSimpleDetails
import com.quanticheart.monitor.project.model.SimpleMobileDetails

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.showNortification.setOnClickListener {
            debugNotification("teste")
        }

        binding.startAlarm.setOnClickListener {
            startTask(object : TaskListener<SimpleMobileDetails?> {
                override fun background(context: Context): SimpleMobileDetails {
                    val details = context.getSimpleDetails()
                    Log.e("INFO", details.toJson())
                    getInfo()
                    return details
                }

                override fun foreground(data: SimpleMobileDetails?) {
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (!verifyMac()) {
            startActivity(Intent(this, InsertMacActivity::class.java))
        } else {
            val data = getMacAndToken()
            binding.id.text = data.first
            binding.token.text = data.second
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermissions {
//            verifyBackGroundLocationPermission{
//                requestBackGroundLocationPermission()
//            }
        }
    }
}