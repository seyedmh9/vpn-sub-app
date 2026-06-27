package com.example.vpnsub

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var configRecyclerView: RecyclerView
    private lateinit var connectButton: Button
    private lateinit var statusText: TextView

    private val configs = mutableListOf<Config>()
    private var selectedConfig: Config? = null
    private var isConnected = false

    // ======================= سابسکریپشن شما =======================
    // فقط این خط را تغییر دهید:
    private val SUBSCRIPTION_URL = "https://dark-hall-b542.seyed09.workers.dev/seyed?sub=seyed"
    // ============================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        loadSubscription()
    }

    private fun initViews() {
        configRecyclerView = findViewById(R.id.configRecyclerView)
        connectButton = findViewById(R.id.connectButton)
        statusText = findViewById(R.id.statusText)

        configRecyclerView.layoutManager = LinearLayoutManager(this)
        configRecyclerView.adapter = ConfigAdapter(configs) { config ->
            selectedConfig = config
            statusText.text = "کانفیگ انتخاب شده: ${config.name}"
        }

        connectButton.setOnClickListener {
            if (!isConnected) {
                startVpn()
            } else {
                stopVpn()
            }
        }
    }

    private fun loadSubscription() {
        statusText.text = "در حال دانلود سابسکریپشن..."

        Thread {
            try {
                val url = URL(SUBSCRIPTION_URL)
                val content = url.readText()

                // هر خط یک کانفیگ است (base64 یا لینک خام)
                val lines = content.lines().filter { it.trim().isNotEmpty() }

                runOnUiThread {
                    configs.clear()
                    lines.forEachIndexed { index, line ->
                        val name = "کانفیگ ${index + 1}"
                        configs.add(Config(name, line.trim()))
                    }
                    (configRecyclerView.adapter as ConfigAdapter).notifyDataSetChanged()
                    statusText.text = "${configs.size} کانفیگ پیدا شد"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    statusText.text = "خطا در دانلود ساب: ${e.message}"
                    // اضافه کردن کانفیگ دمو در صورت خطا
                    configs.add(Config("دمو - WireGuard", "wireguard://demo"))
                    (configRecyclerView.adapter as ConfigAdapter).notifyDataSetChanged()
                }
            }
        }.start()
    }

    private fun startVpn() {
        if (selectedConfig == null) {
            Toast.makeText(this, "لطفا یک کانفیگ انتخاب کنید", Toast.LENGTH_SHORT).show()
            return
        }

        val prepareIntent = VpnService.prepare(this)
        if (prepareIntent != null) {
            startActivityForResult(prepareIntent, 100)
        } else {
            launchVpnService()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            launchVpnService()
        }
    }

    private fun launchVpnService() {
        val intent = Intent(this, VpnServiceImpl::class.java)
        intent.putExtra("config", selectedConfig?.link)
        startService(intent)

        isConnected = true
        connectButton.text = "قطع اتصال"
        statusText.text = "متصل به: ${selectedConfig?.name}"
    }

    private fun stopVpn() {
        stopService(Intent(this, VpnServiceImpl::class.java))
        isConnected = false
        connectButton.text = "اتصال"
        statusText.text = "اتصال قطع شد"
    }
}