package com.admin_bot.features.messages.model

import com.admin_bot.features.bot_managing.data.BotInfo
import kotlinx.datetime.Instant

abstract class MessageSender(private val botInfo: BotInfo) {
    fun sendMessage(messageText: String) {
        for (chatId in botInfo.chatIds) {
            sendMessage(messageText, chatId)
        }
    }

    abstract fun sendMessage(messageText: String, chatId: Long)
    fun sendMessage(messageText: String, sendAt: Instant) {
        for (chatId in botInfo.chatIds) {
            sendMessage(messageText, chatId, sendAt)
        }
    }

    abstract fun sendMessage(messageText: String, chatId: Long, sendAt: Instant)
}