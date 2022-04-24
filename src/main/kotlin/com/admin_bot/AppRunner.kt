package com.admin_bot

import com.admin_bot.config.AppConfig
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class AppRunner(val config : AppConfig) {
    fun runApp(){
        embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        }.start(wait = true)
    }
}