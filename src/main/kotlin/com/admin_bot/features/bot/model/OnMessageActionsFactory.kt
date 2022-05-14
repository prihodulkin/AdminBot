package com.admin_bot.features.bot.model

interface OnMessageActionsFactory {
    fun getAction(actionType: OnMessageActionType): OnMessageAction
}