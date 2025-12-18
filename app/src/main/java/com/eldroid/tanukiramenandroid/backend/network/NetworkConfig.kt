package com.eldroid.tanukiramenandroid.backend.network

import android.os.Build

object NetworkConfig {

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.PRODUCT == "google_sdk")
    }

    private const val PC_IP = "192.168.1.4"

    val BASE_URL: String
        get() = if (isEmulator()) {
            "http://10.0.2.2:8080"
        } else {
            "http://$PC_IP:8080"
        }

    val FRONTEND_URL: String
        get() = if (isEmulator()) {
            "http://10.0.2.2:5500"
        } else {
            "http://$PC_IP:5500"
        }
}