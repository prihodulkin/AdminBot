package com.admin_bot.features.chat.repository

import com.admin_bot.features.chat.data.ChatDescription
import com.admin_bot.features.chat.data.ChatInfo
import com.admin_bot.features.chat.data.ChatType

interface ChatRepository {
    suspend fun getChatInfo(chatId: Long): ChatInfo
    suspend fun getAllChatsInfo(): List<ChatInfo>
    suspend fun getAllChatsInfo(botId: Long): List<ChatInfo>

    suspend fun addChat(chatDescription: ChatDescription)
    suspend fun deleteChatInfo(chatId: Long)
    suspend fun clear()
}