package com.admin_bot.features.classification.model

import com.admin_bot.features.messages.data.Message

class RegexClassifier(private val regex: Regex) : Classifier {
    override suspend fun isAcceptable(message: Message): Boolean = !regex.containsMatchIn(message.text)
}