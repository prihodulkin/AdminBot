package com.admin_bot.features.chat.data

data class ChatInfo(val id: Int, val title: String, val type: ChatType)

enum class ChatType {
    GROUP,
    CHANNEL
}