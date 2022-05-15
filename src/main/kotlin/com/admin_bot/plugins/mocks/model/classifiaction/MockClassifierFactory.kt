package com.admin_bot.plugins.mocks.model.classifiaction

import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.classification.model.ClassifierFactory
import com.admin_bot.features.classification.model.SubstringClassifier

class MockClassifierFactory : ClassifierFactory {
    companion object {
        const val substringValue = "qwerty"
    }

    override suspend fun createClassifier(
        actionConfig: BotActionConfig
    ) = SubstringClassifier(substringValue)
}