package com.admin_bot

import com.admin_bot.config.MockConfig


fun main() {
    val appRunner = AppRunner(MockConfig())
    appRunner.runApp()
}
