package com.admin_bot.plugins.telegram_bot

import com.admin_bot.features.messages.data.Message
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.text
import dev.inmo.tgbotapi.types.chatIdField
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.types.userIdField
import kotlinx.datetime.Instant

fun ContentMessage<TextContent>.toAppMessage(): Message {
    return Message(
        id = messageId,
        text = text ?: "",
        userId = userIdField.toLong(),
        chatId = chatIdField.toLong(),
        createdAt = Instant.parse(date.toString())
    )
}