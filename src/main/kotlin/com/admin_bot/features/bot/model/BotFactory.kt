package com.admin_bot.features.bot.model

import com.admin_bot.features.bot_managing.data.BotInfo

interface BotFactory {
    fun createBot(botInfo: BotInfo)
}