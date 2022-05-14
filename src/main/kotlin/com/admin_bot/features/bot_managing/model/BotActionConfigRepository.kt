package com.admin_bot.features.bot_managing.model

import com.admin_bot.features.bot_managing.data.BotActionConfig

interface BotActionConfigRepository {
   suspend fun getActionConfig(botId: Long): BotActionConfig
   suspend fun getAllConfigs(): List<BotActionConfig>
   suspend fun saveConfig(botId: Long, config: BotActionConfig)
}