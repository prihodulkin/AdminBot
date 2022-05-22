package com.admin_bot.plugins.python_model_runner

import com.admin_bot.features.classification.data.ClassifierType
import com.lordcodes.turtle.ShellLocation
import com.lordcodes.turtle.shellRun
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


class ModelSocketConnector(
    private val modelPath: String,
    private val port: Int,
    private val classifierType: ClassifierType
) {
    private val selectorManager = ActorSelectorManager(Dispatchers.IO)
    private lateinit var socket: Socket
    private lateinit var sendChannel: ByteWriteChannel
    private lateinit var receiveChannel: ByteReadChannel

    companion object {
        suspend fun createConnector(
            modelPath: String,
            port: Int,
            classifierType: ClassifierType
        ): ModelSocketConnector {
            val connector = ModelSocketConnector(modelPath, port, classifierType)
            while (!connector.establishConnection()) {
            }
            return connector
        }
    }

    suspend fun establishConnection(): Boolean {
        val projectPath =  System.getProperty("user.dir")
        val shellLocation = ShellLocation.HOME.resolve("$projectPath/src/main/")
        val res = shellRun(
            "python",
            listOf(
                "model_runner.py",
                port.toString(),
                classifierType.name,
                modelPath
            ), shellLocation
        )
        delay(1000)
        // TODO удалить или использовать логгер
        println(res)
        socket = aSocket(selectorManager).tcp().connect("127.0.0.1", port)
        sendChannel = socket.openWriteChannel(autoFlush = true)
        sendChannel.writeStringUtf8("hi")
        receiveChannel = socket.openReadChannel()
        val response = receiveChannel.readUTF8Line()
        return response == "hi"
    }

    suspend fun sendAndGetAnswer(message: String): String? {
        sendChannel.writeStringUtf8(message)
        return receiveChannel.readUTF8Line()
    }

    suspend fun dispose(){
        sendChannel.writeStringUtf8("by")
        selectorManager.close()
        socket.close()
    }
}


