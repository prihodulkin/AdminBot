package com.admin_bot.plugins.telegram_bot

import com.admin_bot.features.messages.data.Message
import com.soywiz.klock.DateTime
import dev.inmo.tgbotapi.extensions.utils.asTextMentionTextSource
import dev.inmo.tgbotapi.types.MessageIdentifier
import dev.inmo.tgbotapi.types.chat.Chat
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.types.message.textsources.TextMentionTextSource
import kotlinx.datetime.Instant

fun ContentMessage<TextContent>.toAppMessage(): Message = Message(
    id = messageId,
    text = content.text,
    userId = content.textSources.last().asTextMentionTextSource()!!.user.id.chatId,
    chatId = chat.id.chatId,
    createdAt = Instant.parse(date.toString())
)

