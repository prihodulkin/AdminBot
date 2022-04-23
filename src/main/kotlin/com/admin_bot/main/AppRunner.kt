package com.admin_bot.main

import io.ktor.server.engine.*
import io.ktor.server.netty.*

class AppRunner(val config : AppConfig) {
    fun runApp(){
        embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        }.start(wait = true)
    }
}