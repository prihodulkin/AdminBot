package com.admin_bot.features.bot_managing.model

import com.admin_bot.common.async.ListenableStream
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot.model.OnMessageActionsFactory
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.messages.data.Message

class BotController(
    private var actionConfig: BotActionConfig,
    private val actionsFactory: OnMessageActionsFactory,
    messagesStream: ListenableStream<Message>,
    configStream: ListenableStream<BotActionConfig>,
) {

    private val configSubscription = configStream.listen {
        actionConfig = event
    }

    private val messagesSubscription = messagesStream.listen {
        val universalActionType = actionConfig.universalActionType
        if (universalActionType != OnMessageActionType.NONE) {
            executeActionOfExactType(universalActionType, event)
        } else {
            val actionType = actionConfig.chatsActionTypes[event.chatId]
            if (actionType != null) {
                executeActionOfExactType(actionType, event)
            }
        }
    }

    private fun executeActionOfExactType(actionType: OnMessageActionType, message: Message) {
        val action = actionsFactory.getAction(actionType)
        action.execute(message, actionConfig)
    }

    /**
     * Needed to be called when controller isn't used
     */
    fun dispose() {
        configSubscription.close()
        messagesSubscription.close()
    }
}