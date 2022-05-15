package com.admin_bot.plugins.mocks.model.bot

import com.admin_bot.features.bot.model.NoneAction
import com.admin_bot.features.bot.model.OnMessageAction
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot.model.OnMessageActionsFactory
import com.admin_bot.features.bot_managing.data.BotInfo

class MockOnMessageActionsFactory(
    private val botInfo: BotInfo,
    private val mockOnMessageActionLogger: MockOnMessageActionLogger
) : OnMessageActionsFactory {
    override fun getAction(actionType: OnMessageActionType): OnMessageAction = when (actionType) {
        OnMessageActionType.NONE -> NoneAction()
        OnMessageActionType.BAN -> MockBanAction(botInfo, mockOnMessageActionLogger)
        OnMessageActionType.DELETE -> MockDeleteAction(botInfo, mockOnMessageActionLogger)
        OnMessageActionType.REPLY -> MockReplyAction(botInfo, mockOnMessageActionLogger)
    }
}