package com.admin_bot.features.bot.model

/**
 * Abstraction for bot
 */
abstract class Bot(
    val messageReceiver: MessageReceiver,
    val actionsFactory: OnMessageActionsFactory
)