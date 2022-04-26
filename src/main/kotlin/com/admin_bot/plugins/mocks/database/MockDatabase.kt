package com.admin_bot.plugins.mocks.database

import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.chat.data.ChatInfo
import com.admin_bot.features.messages.data.Message
import com.admin_bot.features.user.data.UserInfo

data class MockDatabase(
    var bots: MutableList<BotInfo>?= mutableListOf(),
    var botPasswords: MutableMap<Int, String>?= mutableMapOf(),
    var oneTimePasswords: MutableMap<Int, String>?= mutableMapOf(),
    var users: MutableList<UserInfo>?= mutableListOf(),
    var chats: MutableList<ChatInfo>?= mutableListOf(),
    var messages: MutableList<Message>?= mutableListOf(),
    var savedMessages: MutableList<Message>?= mutableListOf()

){
    companion object{
        /**
         * Initialize fields of MockDatabase with null to
         * throw exception on access attempt with !! operator
         */
        fun withNullValues(): MockDatabase{
            return MockDatabase(
                bots = null,
                botPasswords = null,
                oneTimePasswords = null,
                users = null,
                chats = null,
                messages = null,
                savedMessages = null
            )
        }
    }
}