package com.admin_bot.features.classification.model

import com.admin_bot.features.messages.data.Message

abstract class Classifier() {
    abstract suspend fun isAcceptable(message: Message): Boolean
}