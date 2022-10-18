package com.quanticheart.broadcasts

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
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
import com.quanticheart.monitor.toolbox.SystemHelper
import com.quanticheart.monitor.toolbox.NetworkMonitor
import com.quanticheart.monitor.toolbox.AppReceiver

class MainActivity : AppCompatActivity(), SystemHelper.ConnectionVerify {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var connectionReceiver: AppReceiver? = null
    private var connectionBackReceiver: NetworkMonitor? = null

    private var snackbar: Snackbar? = null
    private var connected = true
    private var showSnackbar = true

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
        connectionReceiverRegister()
        connectionBackgroundReceiverRegister()
    }

    override fun onStart() {
        super.onStart()
        requestPermissions {
//            verifyBackGroundLocationPermission{
//                requestBackGroundLocationPermission()
//            }
        }
    }

    override fun connectionOK() {
        connected = true
        if (!verifySnackbar()) {
            clearSnackbar()
        }
    }

    override fun connectionFail() {
        connected = false
        if (verifySnackbar() && showSnackbar) {
            showSnackbar()
        }
    }


    private fun showSnackbar() {
        snackbar = Snackbar.make(
            binding.root,
            R.string.msg_no_connection,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar?.show()

    }

    private fun clearSnackbar() {
        snackbar?.dismiss()
        snackbar = null
    }

    private fun verifySnackbar(): Boolean {
        return snackbar == null
    }

    override fun onPause() {
        super.onPause()
        connectionReceiver?.let {
            unregisterReceiver(connectionReceiver)
        }
        connectionBackReceiver?.disable()
        clearSnackbar()
    }

    /**
     * Verify Connect
     */
    @Suppress("DEPRECATION")
    private fun connectionReceiverRegister() {
        SystemHelper(this).setConnectionVerify(this)
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        connectionReceiver = AppReceiver()
        registerReceiver(connectionReceiver, filter)
    }

    private fun connectionBackgroundReceiverRegister() {
        connectionBackReceiver = NetworkMonitor()
        connectionBackReceiver?.enable(this)
    }
}