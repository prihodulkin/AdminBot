package com.admin_bot.features.classification.model

import com.admin_bot.features.classification.data.MessageCategory
import com.admin_bot.features.classification.repository.ClassificationHistoryRepository
import com.admin_bot.features.messages.data.Message
import com.admin_bot.features.train.data.DataSet
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

abstract class Classifier(private val classificationHistoryRepository: ClassificationHistoryRepository) {
    suspend fun classifyAndLog(message: Message) = coroutineScope {
        val result = async { classify(message) }
        classificationHistoryRepository.saveBotDecision(message.id, result.await())
    }
    abstract suspend fun classify(message: Message): MessageCategory
    abstract suspend fun train(dataSet: DataSet)
}