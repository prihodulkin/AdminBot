package com.admin_bot

import com.admin_bot.enviroment.AppEnvironment
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class AppRunner(val config : AppEnvironment) {
    fun runApp(){
        embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        }.start(wait = true)
    }
}