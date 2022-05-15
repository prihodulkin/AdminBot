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

open class MockBanAction(
    val botInfo: BotInfo,
    private val mockOnMessageActionLogger: MockOnMessageActionLogger
) : BanAction(), OnMessageAction {
    override fun execute(message: Message, actionConfig: BotActionConfig) {
        mockOnMessageActionLogger.log(message to this)
    }
}

open class MockDeleteAction(
    val botInfo: BotInfo,
    private val mockOnMessageActionLogger: MockOnMessageActionLogger
) : DeleteAction(), OnMessageAction {
    override fun execute(message: Message, actionConfig: BotActionConfig) {
        mockOnMessageActionLogger.log(message to this)
    }
}

open class MockReplyAction(
    botInfo: BotInfo,
    private val mockOnMessageActionLogger: MockOnMessageActionLogger
) : ReplyAction(), OnMessageAction {
    override fun execute(message: Message, actionConfig: BotActionConfig) {
        mockOnMessageActionLogger.log(message to this)
    }
}