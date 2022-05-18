package com.admin_bot.plugins.mocks.model.bot

import com.admin_bot.features.bot.model.BanAction
import com.admin_bot.features.bot.model.DeleteAction
import com.admin_bot.features.bot.model.OnMessageAction
import com.admin_bot.features.bot.model.ReplyAction
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.messages.data.Message

interface MockOnMessageAction : OnMessageAction{
    val botInfo: BotInfo
}

data class MockBanAction(
    override val botInfo: BotInfo,
    private val mockOnMessageActionLogger: MockOnMessageActionLogger
) : BanAction(), MockOnMessageAction {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        mockOnMessageActionLogger.log(message to this)
    }
}

data class MockDeleteAction(
    override val botInfo: BotInfo,
    private val mockOnMessageActionLogger: MockOnMessageActionLogger
) : DeleteAction(), MockOnMessageAction {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        mockOnMessageActionLogger.log(message to this)
    }
}

data class MockReplyAction(
    override val botInfo: BotInfo,
    private val mockOnMessageActionLogger: MockOnMessageActionLogger
) : ReplyAction(), MockOnMessageAction {
    override suspend fun execute(message: Message, actionConfig: BotActionConfig) {
        mockOnMessageActionLogger.log(message to this)
    }
}