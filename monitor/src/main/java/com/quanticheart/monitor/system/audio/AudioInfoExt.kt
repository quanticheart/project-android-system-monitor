package com.quanticheart.monitor.system.audio

import android.content.Context
import android.media.AudioManager
import android.os.Build

internal fun Context.getAudioDetails(): Audio {
    val mobAudioBean = Audio()
    try {
        val mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mobAudioBean.maxVoiceCall =
            mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
        mobAudioBean.currentVoiceCall =
            mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)
        mobAudioBean.maxSystem = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
        mobAudioBean.currentSystem = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
        mobAudioBean.maxRing = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
        mobAudioBean.currentRing = mAudioManager.getStreamVolume(AudioManager.STREAM_RING)
        mobAudioBean.maxMusic = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        mobAudioBean.currentMusic = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        mobAudioBean.maxAlarm = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        mobAudioBean.currentAlarm = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM)
        mobAudioBean.maxNotifications =
            mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
        mobAudioBean.currentNotifications =
            mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
        mobAudioBean.maxDTMF = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF)
        mobAudioBean.currentDTMF = mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mobAudioBean.maxAccessibility =
                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ACCESSIBILITY)
            mobAudioBean.currentAccessibility =
                mAudioManager.getStreamVolume(AudioManager.STREAM_ACCESSIBILITY)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mobAudioBean.minDTMF = mAudioManager.getStreamMinVolume(AudioManager.STREAM_DTMF)
            mobAudioBean.minNotifications =
                mAudioManager.getStreamMinVolume(AudioManager.STREAM_NOTIFICATION)
            mobAudioBean.minAlarm = mAudioManager.getStreamMinVolume(AudioManager.STREAM_ALARM)
            mobAudioBean.minMusic = mAudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC)
            mobAudioBean.minRing = mAudioManager.getStreamMinVolume(AudioManager.STREAM_RING)
            mobAudioBean.minSystem =
                mAudioManager.getStreamMinVolume(AudioManager.STREAM_SYSTEM)
            mobAudioBean.minVoiceCall =
                mAudioManager.getStreamMinVolume(AudioManager.STREAM_VOICE_CALL)
            mobAudioBean.minAccessibility =
                mAudioManager.getStreamMinVolume(AudioManager.STREAM_ACCESSIBILITY)
        }
    } catch (e: Exception) {
    }
    return mobAudioBean
}
