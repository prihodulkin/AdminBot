package com.admin_bot.common.logging

import com.admin_bot.features.bot.model.OnMessageAction
import com.admin_bot.features.messages.data.Message
import io.ktor.util.logging.*
import org.slf4j.Logger

suspend fun Logger.logError(callback: suspend () -> Unit) {
    try {
        callback()
    } catch (e: Exception) {
        error(e)
    }
}

fun Logger.logAction(action: OnMessageAction, message: Message) {
    info("ACTION $action was executed on $message")
}

fun Logger.logMessage(message: Any) {
    info("NEW MESSAGE: $message")
}
