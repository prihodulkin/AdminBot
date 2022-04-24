package com.admin_bot.features.messages.repository

interface MessagesRepository {
    suspend fun getMessages(botId: Int, pageNumber: Int?, pageSize: Int?)
    suspend fun getMessages(botId: Int, chatId: Int, pageNumber: Int?, pageSize: Int?)
}