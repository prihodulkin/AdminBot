package com.admin_bot.plugins.mocks.model.classifiaction

import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.classification.model.ClassifierFactory
import com.admin_bot.features.classification.model.SubstringClassifier
import com.admin_bot.plugins.mocks.MockGlobals

class MockClassifierFactory : ClassifierFactory {
    override suspend fun createClassifier(
        actionConfig: BotActionConfig
    ) = SubstringClassifier(MockGlobals.classifierSubstringValue)
}