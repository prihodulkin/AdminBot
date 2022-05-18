package com.admin_bot.plugins.telegram_bot

import com.admin_bot.features.bot.model.*
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.messages.data.Message
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.requests.DeleteMessage
import dev.inmo.tgbotapi.requests.chat.members.BanChatMember
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.UserId

class TelegramDeleteAction(private val bot: RequestsExecutor) : DeleteAction() {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        bot.execute(DeleteMessage(ChatId(message.chatId), message.id))
    }
}

class TelegramBanAction(private val bot: RequestsExecutor) : BanAction() {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        bot.execute(BanChatMember(ChatId(message.chatId), UserId(message.userId)))
    }
}

class TelegramReplyAction(private val bot: RequestsExecutor) : BanAction() {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        bot.execute(
            SendTextMessage(
                chatId = ChatId(message.chatId),
                text = actionConfig.replyText ?: "",
                replyToMessageId = message.id
            )
        )
    }
}

class TelegramOnMessageActionsFactory(private val bot: RequestsExecutor) : OnMessageActionsFactory {
    override fun getAction(actionType: OnMessageActionType): OnMessageAction = when (actionType) {
        OnMessageActionType.DELETE -> TelegramDeleteAction(bot)
        OnMessageActionType.REPLY -> TelegramReplyAction(bot)
        OnMessageActionType.BAN -> TelegramBanAction(bot)
        OnMessageActionType.NONE -> NoneAction()
    }
}