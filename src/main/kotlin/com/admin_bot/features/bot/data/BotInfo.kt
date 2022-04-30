package com.admin_bot.features.bot.data

data class BotInfo(
    val id: Int,
    val token: String,
    val adminEmail: String? = null,
    val chatIds: Set<Int> = mutableSetOf(),
)