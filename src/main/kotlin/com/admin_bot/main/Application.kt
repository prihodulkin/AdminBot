package com.admin_bot.main

import com.admin_bot.config.mock.MockConfig


fun main() {
    val appRunner = AppRunner(MockConfig())
    appRunner.runApp()
}
