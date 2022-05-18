package com.admin_bot.features.bot.model

import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.messages.data.Message

/**
 * Type of action can be executed when message is received
 */
enum class OnMessageActionType {

    /**
     * Ban user which sent message
     */
    BAN,

    /**
     * Delete message
     */
    DELETE,

    /**
     * Reply to user which sent message
     */
    REPLY,

    /**
     * Do nothing
     */
    NONE,

}

/**
 * Action can be executed when message is received
 */
interface OnMessageAction {
    suspend fun execute(message: Message, actionConfig: BotActionConfig)
    val type: OnMessageActionType
}


/**
 * Executes ban of user which sent message
 */
abstract class BanAction : OnMessageAction {
    final override val type: OnMessageActionType
        get() = OnMessageActionType.BAN
}

/**
 * Executes message deleting
 */
abstract class DeleteAction : OnMessageAction {
    final override val type: OnMessageActionType
        get() = OnMessageActionType.DELETE
}

/**
 * Executes replying to user which sent message
 */
abstract class ReplyAction : OnMessageAction {
    final override val type: OnMessageActionType
        get() = OnMessageActionType.REPLY
}

/**
 * Executes nothing
 */
class NoneAction : OnMessageAction {
   override suspend fun execute(message: Message, actionConfig: BotActionConfig) {}

    final override val type: OnMessageActionType
        get() = OnMessageActionType.NONE
}



