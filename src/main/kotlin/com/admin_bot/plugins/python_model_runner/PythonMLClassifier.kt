package com.admin_bot.plugins.python_model_runner

import com.admin_bot.features.classification.data.ClassifierType
import com.admin_bot.features.classification.model.Classifier
import com.admin_bot.features.messages.data.Message

class PythonMLClassifier(
    private val socketConnector: ModelSocketConnector,
) : Classifier {



    override suspend fun isAcceptable(message: Message): Boolean {
        TODO("Not yet implemented")
    }


}