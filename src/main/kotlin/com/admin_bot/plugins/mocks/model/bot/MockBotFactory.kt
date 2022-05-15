package com.admin_bot.plugins.mocks.model.bot

import com.admin_bot.features.bot.model.Bot
import com.admin_bot.features.bot.model.BotFactory
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.plugins.mocks.database.MockDatabase

class MockBotFactory(
    private val onMessageActionLogger: MockOnMessageActionLogger,
    private val mockDatabase: MockDatabase
) : BotFactory {
    override fun createBot(botInfo: BotInfo): Bot =
        MockBot(
            mockDatabase.messageReceivers!![botInfo.id]!!,
            MockOnMessageActionsFactory(botInfo, onMessageActionLogger)
        )
}