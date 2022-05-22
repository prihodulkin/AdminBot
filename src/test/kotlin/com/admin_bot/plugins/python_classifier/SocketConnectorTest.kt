package com.admin_bot.plugins.python_classifier

import com.admin_bot.features.classification.data.ClassifierType
import com.admin_bot.features.messages.data.Message
import com.lordcodes.turtle.ShellLocation
import com.lordcodes.turtle.shellRun
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.junit.Test
import kotlin.test.assertEquals

class SocketConnectorTest {
    @Test
    fun testClassifierRunnerPy() = runBlocking {
        val projectPath = System.getProperty("user.dir")
        val shellLocation = ShellLocation.HOME.resolve("$projectPath/src/main/")
        GlobalScope.launch {
            shellRun(
                "python",
                listOf(
                    "classifier_runner.py",
                    "12345",
                    "MOCK",
                    "path",
                ), shellLocation
            )
        }
        val selectorManager = ActorSelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 12345)

        val sendChannel = socket.openWriteChannel(autoFlush = true)
        val receiveChannel = socket.openReadChannel()
        sendChannel.writeStringUtf8("hi")

        var response = receiveChannel.readUTF8Line()
        assertEquals("hi", response)

        sendChannel.writeStringUtf8("qwerty aaaaaa")
        response = receiveChannel.readUTF8Line()
        assertEquals("False", response)

        sendChannel.writeStringUtf8("qwert aaaa")
        response = receiveChannel.readUTF8Line()
        assertEquals("True", response)

        sendChannel.writeStringUtf8("bye")
        response = receiveChannel.readUTF8Line()
        assertEquals("bye", response)

        selectorManager.close()
        socket.close()
    }

    @Test
    fun testSocketConnector() = runBlocking {
        val socketConnector = SocketConnector.createConnector(
            "path",
            12345,
            ClassifierType.MOCK
        )
        assertEquals(socketConnector.sendAndGetAnswer("qwerty a"), "False")
        assertEquals(socketConnector.sendAndGetAnswer("qwert a"), "True")
        socketConnector.dispose()
    }

    @Test
    fun testPythonMLClassifier() = runBlocking {
        val socketConnector = SocketConnector.createConnector(
            "path",
            12345,
            ClassifierType.MOCK
        )
        val classifier = PythonMLClassifier(socketConnector)
        try{
            val badMessage = Message(1,"qwerty a",1,1, Clock.System.now())
            val goodMessage = Message(1,"qwert a",1,1, Clock.System.now())
            assertEquals(classifier.isAcceptable(badMessage), false)
            assertEquals(classifier.isAcceptable(goodMessage), true)
        } catch (e: Exception){
            throw e
        } finally {
            socketConnector.dispose()
        }
    }
}