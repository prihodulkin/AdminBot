package com.admin_bot.features.bot.model

/**
 * Abstraction for bot
 */
abstract class Bot {
    abstract val messageReceiver: MessageReceiver
    abstract val actionsFactory: OnMessageActionsFactory
}