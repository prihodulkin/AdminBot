package com.admin_bot.features.bot_managing.model

import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotInfo

interface BotInfoRepository {
   suspend fun getInfo(botId: Long): BotInfo
   suspend fun getAllInfo(): List<BotInfo>
   suspend fun saveConfig(botId: Long, config: BotActionConfig)
}