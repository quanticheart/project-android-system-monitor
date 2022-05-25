package com.quanticheart.monitor.system.system

import android.content.Intent
import android.os.BatteryManager
import android.util.Log

private fun battery(intent: Intent) {
    val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
    val isCharging =
        status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
   Log.e("","Battery is changing: $isCharging")
    if (isCharging) {
//            mTextCarregando.setText("Carregando");
    } else {
//            mTextCarregando.setText("Não Carregando");
    }


    //////////////////////////////////////////////////////////////////////
    val chargePlug: Int = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
    val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
    when {
        usbCharge -> {
//            mTextTipo.setText("Carregando Via USB");
        }
        acCharge -> {
//            mTextTipo.setText("Carregando Via Tomada");
        }
        else -> {
//            mTextTipo.setText("Não Carregando");
        }
    }
   Log.e("","Battery:  usbCharge: $usbCharge , acCharge: $acCharge")

    ///////////////////////////////////////////////////////////////////////
    val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
   Log.e("","Battery:  scale: $scale")
    //        mTextViewInfo.setText("Battery Scale : " + scale);
    val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
   Log.e("","Battery:  level: $level")
    //        mTextViewInfo.setText(mTextViewInfo.getText() + "\nBattery Level : " + level);
    val percentage = level / scale.toFloat()
    val mProgressStatus = (percentage * 100).toInt()
   Log.e("","Battery:  ProgressStatus: $mProgressStatus%")

//        mProgressStatus = (int) ((percentage) * 100);
//
//        mTextViewPercentage.setText("" + mProgressStatus + "%");
//
//        mTextViewInfo.setText(mTextViewInfo.getText() + "\nPercentage : " + mProgressStatus + "%");
//
//        mProgressBar.setProgress(mProgressStatus);
}
