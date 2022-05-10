package com.admin_bot.features.chat.data

data class ChatInfo(val id: Long, val title: String, val type: ChatType)

enum class ChatType {
    GROUP,
    CHANNEL
}