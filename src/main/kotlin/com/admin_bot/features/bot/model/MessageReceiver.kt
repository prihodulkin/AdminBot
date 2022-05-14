package com.admin_bot.features.bot.model

import com.admin_bot.common.async.ListenableStream
import com.admin_bot.features.messages.data.Message

interface MessageReceiver {
    val messages: ListenableStream<Message>
}