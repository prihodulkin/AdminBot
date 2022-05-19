package com.admin_bot.plugins.telegram_bot

import com.admin_bot.features.messages.data.Message
import dev.inmo.tgbotapi.extensions.utils.asFromUser
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import kotlinx.datetime.Instant

fun ContentMessage<TextContent>.toAppMessage(): Message = Message(
    id = messageId,
    text = content.text,
    userId = asFromUser()!!.user.id.chatId,
    chatId = chat.id.chatId,
    createdAt = Instant.fromEpochMilliseconds(date.unixMillisLong)
)

