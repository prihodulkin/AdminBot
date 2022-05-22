package com.admin_bot.plugins.python_classifier

import com.admin_bot.features.classification.model.Classifier
import com.admin_bot.features.messages.data.Message

class PythonMLClassifier(
    private val socketConnector: SocketConnector,
) : Classifier {


    override suspend fun isAcceptable(message: Message): Boolean {
        return socketConnector.sendAndGetAnswer(message.text)=="True"
    }

    override suspend fun dispose() {
        socketConnector.dispose()
    }


}