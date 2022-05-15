package com.admin_bot.plugins.mocks.database

import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.chat.data.ChatInfo
import com.admin_bot.features.classification.data.ClassifierType
import com.admin_bot.features.classification.model.Classifier
import com.admin_bot.features.messages.data.Message
import com.admin_bot.features.registration.data.OtpData
import com.admin_bot.features.user.data.UserInfo
import com.admin_bot.plugins.mocks.model.bot.MockMessageReceiver

data class MockDatabase(
    var bots: MutableList<BotInfo>? = mutableListOf(),
    var botPasswords: MutableMap<Long, String>? = mutableMapOf(),
    var oneTimePasswords: MutableMap<Long, OtpData>? = mutableMapOf(),
    var users: MutableList<UserInfo>? = mutableListOf(),
    var chats: MutableList<ChatInfo>? = mutableListOf(),
    var messages: MutableList<Message>? = mutableListOf(),
    var savedMessages: MutableList<Message>? = mutableListOf(),
    var classifiers: MutableMap<Long, MutableMap<ClassifierType, Classifier>>? = mutableMapOf(),
    var classifier: Classifier? = null,
    var messageReceivers: Map<Long, MockMessageReceiver>? = null
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
                savedMessages = null,
                classifiers = null,
                classifier = null,
            )
        }
    }
}