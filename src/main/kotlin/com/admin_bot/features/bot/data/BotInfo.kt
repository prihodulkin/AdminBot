package com.admin_bot.features.bot.data

data class BotInfo(val id: Int, val accessToken: String, val adminEmail: String, val chatIds: Set<Int>)