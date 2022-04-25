package com.admin_bot

import com.admin_bot.enviroment.MockEnvironment


fun main() {
    val appRunner = AppRunner(MockEnvironment())
    appRunner.runApp()
}
