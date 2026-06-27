package com.example.vpnsub

import android.content.Intent
import android.net.VpnService

class VpnServiceImpl : VpnService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val configLink = intent?.getStringExtra("config") ?: return START_NOT_STICKY

        // استفاده از هسته sing-box
        SingBoxManager.startVpn(this, configLink)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        SingBoxManager.stopVpn()
    }
}