package com.admin_bot.features.bot_managing.data

import com.admin_bot.features.bot.model.OnMessageActionType

/**
 * Configuration for actions which bot executes on received messages
 */
data class BotActionConfig(
    val enabled: Boolean = false,
    val universalActionType: OnMessageActionType = OnMessageActionType.NONE,
    val chatsActionTypes: Map<Long, OnMessageActionType> = mutableMapOf(),
    val replyText: String = "",
)