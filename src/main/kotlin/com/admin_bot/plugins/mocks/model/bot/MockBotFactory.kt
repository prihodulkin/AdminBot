package com.admin_bot.plugins.mocks.model.bot

import com.admin_bot.features.bot.model.Bot
import com.admin_bot.features.bot.model.BotFactory
import com.admin_bot.features.bot_managing.data.BotInfo

class MockBotFactory(private val onMessageActionLogger: MockOnMessageActionLogger) : BotFactory {
    override fun createBot(botInfo: BotInfo): Bot =
        MockBot(MockMessageReceiver(), MockOnMessageActionsFactory(botInfo, onMessageActionLogger))
}