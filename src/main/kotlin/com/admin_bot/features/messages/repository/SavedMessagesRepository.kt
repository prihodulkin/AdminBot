package com.admin_bot.features.messages.repository

import com.admin_bot.features.messages.data.Message

interface SavedMessagesRepository {
    suspend fun getMessages(botId: Long, pageNumber: Long?, pageSize: Long?)
    suspend fun getMessages(botId: Long, chatId: Long, pageNumber: Long?, pageSize: Long?)
    suspend fun addMessage(botId: Long, message: Message)
    suspend fun deleteMessage(messageId: Long)
    suspend fun clear()
}