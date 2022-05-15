package com.admin_bot.features.classification.model

import com.admin_bot.features.classification.data.ClassifierType

interface ClassifierRepository {
    suspend fun getClassifier(classifierType: ClassifierType, botId: Long): Classifier
}