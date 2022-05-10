package com.admin_bot.features.bot.repository

import com.admin_bot.features.bot.data.BotAction
import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.classification.data.MessageCategory

interface BotRepository {
    suspend fun getBotInfo(botId: Long): BotInfo
    suspend fun addAdminEmail(botId: Long, email: String)
    suspend fun addChat(botId: Long, chatId: Long)
    suspend fun deleteChat(botId: Long, chatId: Long)

    suspend fun setAction(messagesCategory: MessageCategory, botAction: BotAction, botId: Long)

    suspend fun clear()
}