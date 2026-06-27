package com.example.vpnsub

import android.content.Context
import android.net.VpnService
import io.nekohasekai.sfa.bg.BoxService
import io.nekohasekai.sfa.data.Options
import io.nekohasekai.sfa.data.SingBoxOptions
import org.json.JSONObject

object SingBoxManager {

    private var boxService: BoxService? = null

    fun startVpn(context: Context, configLink: String) {
        val options = Options()

        // تبدیل کانفیگ به فرمت sing-box
        val singBoxConfig = convertToSingBoxConfig(configLink)

        options.config = singBoxConfig.toString()
        options.protect = true

        boxService = BoxService(context, options)
        boxService?.start()
    }

    fun stopVpn() {
        boxService?.stop()
        boxService = null
    }

    private fun convertToSingBoxConfig(configLink: String): JSONObject {
        val json = JSONObject()

        // ساختار پایه sing-box
        json.put("log", JSONObject().apply {
            put("level", "info")
        })

        json.put("dns", JSONObject().apply {
            put("servers", listOf("8.8.8.8", "1.1.1.1"))
        })

        json.put("inbounds", listOf(
            JSONObject().apply {
                put("type", "tun")
                put("inet4_address", "10.0.0.2/32")
                put("mtu", 1500)
                put("auto_route", true)
                put("stack", "system")
            }
        ))

        // اینجا کانفیگ اصلی (VLESS / VMess / Trojan) را parse می‌کنیم
        val outbound = parseConfigToOutbound(configLink)
        json.put("outbounds", listOf(outbound))

        json.put("route", JSONObject().apply {
            put("rules", listOf<JSONObject>())
            put("final", "proxy")
        })

        return json
    }

    private fun parseConfigToOutbound(link: String): JSONObject {
        return when {
            link.startsWith("vless://") -> parseVless(link)
            link.startsWith("vmess://") -> parseVmess(link)
            link.startsWith("trojan://") -> parseTrojan(link)
            link.startsWith("wireguard://") -> parseWireGuard(link)
            else -> JSONObject().apply {
                put("type", "direct")
            }
        }
    }

    private fun parseVless(link: String): JSONObject {
        // مثال ساده parse کردن VLESS
        return JSONObject().apply {
            put("type", "vless")
            put("tag", "proxy")
            put("server", "example.com")
            put("server_port", 443)
            put("uuid", "your-uuid-here")
            put("flow", "")
            put("tls", JSONObject().apply {
                put("enabled", true)
                put("server_name", "example.com")
            })
        }
    }

    private fun parseVmess(link: String): JSONObject {
        return JSONObject().apply {
            put("type", "vmess")
            put("tag", "proxy")
            put("server", "example.com")
            put("server_port", 443)
            put("uuid", "your-uuid")
            put("security", "auto")
        }
    }

    private fun parseTrojan(link: String): JSONObject {
        return JSONObject().apply {
            put("type", "trojan")
            put("tag", "proxy")
            put("server", "example.com")
            put("server_port", 443)
            put("password", "your-password")
            put("tls", JSONObject().apply { put("enabled", true) })
        }
    }

    private fun parseWireGuard(link: String): JSONObject {
        return JSONObject().apply {
            put("type", "wireguard")
            put("tag", "proxy")
            put("server", "example.com")
            put("server_port", 51820)
            put("private_key", "your-private-key")
            put("peer_public_key", "peer-public-key")
        }
    }
}