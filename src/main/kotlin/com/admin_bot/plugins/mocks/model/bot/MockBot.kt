package com.admin_bot.plugins.mocks.model.bot

import com.admin_bot.features.bot.model.Bot
import com.admin_bot.features.bot.model.MessageReceiver
import com.admin_bot.features.bot.model.OnMessageActionsFactory

class MockBot(
    override val messageReceiver: MessageReceiver,
    override val actionsFactory: OnMessageActionsFactory
) : Bot()