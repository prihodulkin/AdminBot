package com.admin_bot.plugins.python_model_runner

import com.admin_bot.features.classification.data.ClassifierType
import com.admin_bot.features.classification.model.Classifier
import com.admin_bot.features.classification.model.ClassifierRepository

abstract class PythonClassifierRepository : ClassifierRepository {
    protected abstract suspend fun getModelPath(classifierType: ClassifierType, botId: Long): String
    private fun getAvailablePort(botId: Long): Int{
        // TODO добавить распределение портов
        return 12345
    }

    override suspend fun getClassifier(classifierType: ClassifierType, botId: Long): Classifier {
        val path = getModelPath(classifierType, botId)
        val port = getAvailablePort(botId)
        val connector = ModelSocketConnector.createConnector(path,port, classifierType)
        return PythonMLClassifier(connector)
    }
}

class MockPythonClassifierRepository: PythonClassifierRepository(){
    override suspend fun getModelPath(classifierType: ClassifierType, botId: Long): String {
        TODO("Not yet implemented")
    }

}