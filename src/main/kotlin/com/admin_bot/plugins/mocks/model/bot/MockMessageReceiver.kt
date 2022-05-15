package com.admin_bot.plugins.mocks.model.bot

import com.admin_bot.common.async.ListenableStream
import com.admin_bot.common.async.Stream
import com.admin_bot.features.bot.model.MessageReceiver
import com.admin_bot.features.messages.data.Message

class MockMessageReceiver : MessageReceiver {
    private val pMessages = Stream<Message>()
    override val messages: ListenableStream<Message>
        get() = pMessages

    fun addMessage(message: Message) = pMessages.add(message)
}