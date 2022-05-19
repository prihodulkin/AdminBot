package com.admin_bot.plugins.telegram_bot

import com.admin_bot.common.logging.logAction
import com.admin_bot.features.bot.model.*
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.messages.data.Message
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.requests.DeleteMessage
import dev.inmo.tgbotapi.requests.chat.members.BanChatMember
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.UserId
import org.slf4j.Logger

class TelegramDeleteAction(
    private val bot: RequestsExecutor,
    private val logger: Logger,
    private val isLoggingEnabled: Boolean = false
) : DeleteAction() {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        bot.execute(DeleteMessage(ChatId(message.chatId), message.id))
        if(isLoggingEnabled){
            logger.logAction(this, message)
        }
    }
}

class TelegramBanAction(
    private val bot: RequestsExecutor, private val logger: Logger,
    private val isLoggingEnabled: Boolean = false
) : BanAction() {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        bot.execute(BanChatMember(ChatId(message.chatId), UserId(message.userId)))
        if(isLoggingEnabled){
            logger.logAction(this, message)
        }
    }
}

class TelegramReplyAction(
    private val bot: RequestsExecutor, private val logger: Logger,
    private val isLoggingEnabled: Boolean = false
) : BanAction() {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        bot.execute(
            SendTextMessage(
                chatId = ChatId(message.chatId),
                text = actionConfig.replyText ?: "",
                replyToMessageId = message.id
            )
        )
        if(isLoggingEnabled){
            logger.logAction(this, message)
        }
    }
}

class TelegramOnMessageActionsFactory(
    private val bot: RequestsExecutor,
    private val logger: Logger,
    private val isLoggingEnabled: Boolean = false
) : OnMessageActionsFactory {
    override fun getAction(actionType: OnMessageActionType): OnMessageAction = when (actionType) {
        OnMessageActionType.DELETE -> TelegramDeleteAction(bot, logger, isLoggingEnabled)
        OnMessageActionType.REPLY -> TelegramReplyAction(bot, logger, isLoggingEnabled)
        OnMessageActionType.BAN -> TelegramBanAction(bot, logger, isLoggingEnabled)
        OnMessageActionType.NONE -> NoneAction()
    }
}