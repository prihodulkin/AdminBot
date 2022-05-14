package com.admin_bot.features.bot_managing.data

import com.admin_bot.features.bot.model.OnMessageActionType
import kotlinx.serialization.Serializable

@Serializable
data class BotActionConfigChange(
    private val enabled: Boolean?,
    private val universalActionType: OnMessageActionType?,
    private val chatsActionTypes: Map<Long, OnMessageActionType>?,
    private val replyText: String? = null,
) {
    fun apply(actionConfig: BotActionConfig): BotActionConfig = actionConfig.copy(
        enabled = enabled ?: actionConfig.enabled,
        universalActionType = universalActionType ?: actionConfig.universalActionType,
        chatsActionTypes = chatsActionTypes ?: actionConfig.chatsActionTypes,
        replyText = replyText ?: actionConfig.replyText
    )
}