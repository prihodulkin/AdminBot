package com.admin_bot.features.classification.model

import com.admin_bot.features.bot_managing.data.BotActionConfig

interface ClassifierFactory {
    suspend fun createClassifier(actionConfig: BotActionConfig): Classifier
}