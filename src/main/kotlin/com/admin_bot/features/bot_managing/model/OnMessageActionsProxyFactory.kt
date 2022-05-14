package com.admin_bot.features.bot_managing.model

import com.admin_bot.features.bot.model.OnMessageAction
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot.model.OnMessageActionsFactory

class OnMessageActionsCachingProxyFactory(
    private val factory: OnMessageActionsFactory
) : OnMessageActionsFactory {
    private val actions = mutableMapOf<OnMessageActionType, OnMessageAction>()
    override fun getAction(actionType: OnMessageActionType): OnMessageAction {
        return if (actions.containsKey(actionType)) {
            actions[actionType]!!
        } else {
            val action = factory.getAction(actionType)
            actions[actionType] = action
            action
        }
    }

}