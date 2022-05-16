package com.admin_bot.features.bot_managing.data

import com.admin_bot.features.bot.model.OnMessageActionType
import kotlinx.serialization.Serializable

@Serializable
data class BotActionConfigChange(
    val enabled: Boolean? = null,
    val universalActionType: OnMessageActionType?=null,
    val chatsActionTypes: Map<Long, OnMessageActionType>?=null,
    val replyText: String? = null,
) {
    fun apply(actionConfig: BotActionConfig): BotActionConfig = actionConfig.copy(
        enabled = enabled ?: actionConfig.enabled,
        universalActionType = universalActionType ?: actionConfig.universalActionType,
        chatsActionTypes = chatsActionTypes ?: actionConfig.chatsActionTypes,
        replyText = replyText ?: actionConfig.replyText
    )
}