package com.admin_bot.plugins.python_classifier

import com.admin_bot.features.classification.data.ClassifierType
import com.admin_bot.features.classification.model.Classifier
import com.admin_bot.features.classification.model.ClassifierRepository
import java.net.ServerSocket

abstract class PythonClassifierRepository : ClassifierRepository {
    protected abstract suspend fun getModelPath(classifierType: ClassifierType, botId: Long): String
    private fun getAvailablePort(): Int{
        val serverSocket = ServerSocket(0)
        serverSocket.close()
        return serverSocket.localPort
    }

    override suspend fun getClassifier(classifierType: ClassifierType, botId: Long): Classifier {
        val path = getModelPath(classifierType, botId)
        val port = getAvailablePort()
        val connector = SocketConnector.createConnector(path,port, classifierType)
        return PythonMLClassifier(connector)
    }
}

class MockPythonClassifierRepository: PythonClassifierRepository(){
    override suspend fun getModelPath(classifierType: ClassifierType, botId: Long): String {
        return "path"
    }
}