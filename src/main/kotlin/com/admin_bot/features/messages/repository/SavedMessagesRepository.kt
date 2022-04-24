package com.admin_bot.features.messages.repository

import com.admin_bot.features.messages.data.Message

interface SavedMessagesRepository {
    suspend fun getMessages(botId: Int, pageNumber: Int?, pageSize: Int?)
    suspend fun getMessages(botId: Int, chatId: Int, pageNumber: Int?, pageSize: Int?)
    suspend fun addMessage(botId: Int, message: Message)
    suspend fun deleteMessage(messageId: Int)
    suspend fun clear()
}