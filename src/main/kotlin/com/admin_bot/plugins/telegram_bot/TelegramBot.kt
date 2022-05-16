package com.admin_bot.plugins.telegram_bot

import com.admin_bot.common.async.ListenableStream
import com.admin_bot.common.async.Stream
import com.admin_bot.features.bot.model.Bot
import com.admin_bot.features.bot.model.MessageReceiver
import com.admin_bot.features.bot.model.OnMessageActionsFactory
import com.admin_bot.features.messages.data.Message
import dev.inmo.micro_utils.coroutines.CoroutineScope
import dev.inmo.micro_utils.coroutines.subscribe
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.utils.shortcuts.textMessages
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPolling
import kotlinx.coroutines.Dispatchers

class TelegramBot(val token: String): Bot() {
    private val bot = telegramBot(token)
    override val messageReceiver = TelegramMessageReceiver(bot)


    override val actionsFactory: OnMessageActionsFactory
        get() = TODO("Not yet implemented")

}

class TelegramMessageReceiver(private val bot: RequestsExecutor): MessageReceiver{
    private val pMessages = Stream<Message>()
    private val scope = CoroutineScope(Dispatchers.Default) {}
    override val messages: ListenableStream<Message>
        get() = pMessages
    init {
        bot.longPolling {
            textMessages().subscribe(scope) {
                println(it)
                pMessages.add(it.toAppMessage())

            }
        }
    }
}