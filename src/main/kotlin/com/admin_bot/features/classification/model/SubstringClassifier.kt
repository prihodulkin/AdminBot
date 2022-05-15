package com.admin_bot.features.classification.model

import com.admin_bot.features.messages.data.Message

class SubstringClassifier(private val substring: String): Classifier {
    override suspend fun isAcceptable(message: Message): Boolean = !message.text.contains(substring)
}