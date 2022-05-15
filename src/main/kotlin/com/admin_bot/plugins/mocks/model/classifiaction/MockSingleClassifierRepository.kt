package com.admin_bot.plugins.mocks.model.classifiaction

import com.admin_bot.features.classification.data.ClassifierType
import com.admin_bot.features.classification.model.Classifier
import com.admin_bot.features.classification.model.ClassifierRepository
import com.admin_bot.features.classification.model.SubstringClassifier

class MockSingleClassifierRepository : ClassifierRepository {
    companion object {
        const val substringValue = "qwerty"
    }

    override suspend fun getClassifier(
        classifierType: ClassifierType,
        botId: Long
    ): Classifier = SubstringClassifier(
        substringValue
    )

}