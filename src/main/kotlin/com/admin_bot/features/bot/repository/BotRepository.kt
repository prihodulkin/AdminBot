package com.admin_bot.features.bot.repository

import com.admin_bot.features.bot.data.BotAction
import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.classification.data.MessageCategory

interface BotRepository {
    suspend fun getBotInfo(botId: Int): BotInfo
    suspend fun addAdminEmail(botId: Int, email: String)
    suspend fun addChat(botId: Int, chatId: Int)
    suspend fun deleteChat(botId: Int, chatId: Int)

    suspend fun setAction(messagesCategory: MessageCategory, botAction: BotAction, botId: Int)

    suspend fun clear()
}