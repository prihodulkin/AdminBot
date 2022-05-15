package com.admin_bot.plugins.mocks.model.bot_managing

import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.bot_managing.model.BotInfoRepository
import com.admin_bot.plugins.mocks.database.MockDatabase

class MockBotInfoRepository(private val mockDatabase: MockDatabase) : BotInfoRepository {
    override suspend fun getInfo(botId: Long): BotInfo =
        mockDatabase.bots!!.first { bot -> bot.id == botId }

    override suspend fun getAllInfo(): List<BotInfo> = mockDatabase.bots!!

    override suspend fun saveConfig(botId: Long, config: BotActionConfig) {
        val bots = mockDatabase.bots!!
        val index=  bots.indexOfFirst{ bot -> bot.id == botId }
        bots[index] = bots[index].copy(actionConfig = config)
    }
}