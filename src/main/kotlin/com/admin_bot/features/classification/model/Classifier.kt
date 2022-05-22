package com.admin_bot.features.classification.model

import com.admin_bot.features.messages.data.Message

interface Classifier {
    suspend fun isAcceptable(message: Message): Boolean
    suspend fun dispose(){}
}