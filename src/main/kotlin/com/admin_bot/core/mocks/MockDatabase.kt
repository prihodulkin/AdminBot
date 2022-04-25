package com.admin_bot.core.mocks

import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.chat.data.ChatInfo
import com.admin_bot.features.messages.data.Message
import com.admin_bot.features.user.data.UserInfo

data class MockDatabase(
    val bots: MutableList<BotInfo>?,
    val botPasswords: MutableMap<Int, String>?,
    val oneTimePasswords: MutableMap<Int, String>?,
    val users: MutableList<UserInfo>?,
    val chats: MutableList<ChatInfo>?,
    val messages: MutableList<Message>?,
    val savedMessages: MutableList<Message>?
)