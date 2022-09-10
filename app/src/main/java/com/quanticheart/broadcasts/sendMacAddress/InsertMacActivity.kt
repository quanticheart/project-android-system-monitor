package com.quanticheart.broadcasts.sendMacAddress

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.quanticheart.broadcasts.databinding.MacMainBinding
import com.quanticheart.monitor.extentions.isValidMacAddress
import com.quanticheart.monitor.project.system.uuid

class InsertMacActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(InsertMacViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MacMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            val macAddress = binding.edt.text.toString()
            if (macAddress.isValidMacAddress()) {
                val uuid = uuid
                viewModel.insertMacAddress(uuid, macAddress)
            }
        }

        viewModel.insertSuccess.observe(this) {
            if (it == true) {
                Toast.makeText(this, "inserido com success", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.insertError.observe(this) {
            if (it == true) {
                Toast.makeText(this, viewModel.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
