package com.admin_bot.plugins.telegram_bot

import com.admin_bot.common.async.ListenableStream
import com.admin_bot.common.async.Stream
import com.admin_bot.features.bot.model.Bot
import com.admin_bot.features.bot.model.BotFactory
import com.admin_bot.features.bot.model.MessageReceiver
import com.admin_bot.features.bot.model.OnMessageActionsFactory
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.messages.data.Message
import dev.inmo.micro_utils.coroutines.CoroutineScope
import dev.inmo.micro_utils.coroutines.subscribe
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.utils.shortcuts.textMessages
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPolling
import kotlinx.coroutines.Dispatchers
import org.slf4j.ILoggerFactory
import org.slf4j.Logger

class TelegramBotFactory(
    private val loggerFactory: ILoggerFactory,
    private val isLoggingEnabled: Boolean = false
) :
    BotFactory {
    override fun createBot(botInfo: BotInfo): Bot =
        TelegramBot(
            botInfo.token,
            loggerFactory.getLogger(TelegramBot::class.java.name),
            isLoggingEnabled,
        )
}

class TelegramBot(
    val token: String,
    logger: Logger,
    isLoggingEnabled: Boolean = false
) :
    Bot() {
    private val bot = telegramBot(token)
    override val messageReceiver = TelegramMessageReceiver(bot, logger, isLoggingEnabled)
    override val actionsFactory: OnMessageActionsFactory = TelegramOnMessageActionsFactory(bot)
}

class TelegramMessageReceiver(
    bot: RequestsExecutor,
    private val logger: Logger,
    private val isLoggingEnabled: Boolean = false
) : MessageReceiver {
    private val pMessages = Stream<Message>()
    private val scope = CoroutineScope(Dispatchers.Default) {}
    override val messages: ListenableStream<Message>
        get() = pMessages

    init {
        bot.longPolling {
            textMessages().subscribe(scope) {
                if(isLoggingEnabled){
                    logger.info("New message: $it")
                }
                pMessages.add(it.toAppMessage())

            }
        }
    }
}