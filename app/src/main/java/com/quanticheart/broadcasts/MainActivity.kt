package com.quanticheart.broadcasts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.broadcasts.databinding.ActivityMainBinding
import com.quanticheart.broadcasts.sendMacAddress.InsertMacActivity
import com.quanticheart.monitor.system.asyncTask.TaskListener
import com.quanticheart.monitor.system.asyncTask.startTask
import com.quanticheart.monitor.system.extentions.toJson
import com.quanticheart.monitor.system.notification.debugNotification
import com.quanticheart.monitor.system.permissions.requestPermissions
import com.quanticheart.monitor.system.project.getInfo
import com.quanticheart.monitor.system.project.getSimpleDetails
import com.quanticheart.monitor.system.project.model.SimpleMobileDetails

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
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