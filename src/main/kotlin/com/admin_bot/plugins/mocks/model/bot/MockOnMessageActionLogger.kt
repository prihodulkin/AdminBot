package com.admin_bot.plugins.mocks.model.bot

import com.admin_bot.features.bot.model.OnMessageAction
import com.admin_bot.features.messages.data.Message


class MockOnMessageActionLogger {
    private val pLoggedItems = mutableListOf<Pair<Message, MockOnMessageAction>>()

    fun log(logItem: Pair<Message, MockOnMessageAction>){
        pLoggedItems.add(logItem)
    }

    val loggedItems: List<Pair<Message, MockOnMessageAction>>
            get() = pLoggedItems
}