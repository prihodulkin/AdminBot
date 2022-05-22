package com.admin_bot

import com.admin_bot.confidential.BotTokens
import com.admin_bot.environment.TestEnvironment
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.classification.data.ClassifierType
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.python_model_runner.ModelSocketConnector
import com.admin_bot.plugins.telegram_bot.TelegramBotFactory
import com.admin_bot.runner.AppRunner
import com.lordcodes.turtle.ShellLocation
import com.lordcodes.turtle.shellRun
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory


fun main() = runBlocking {

    val connector = ModelSocketConnector("",12346, ClassifierType.ML)
    val res = connector.establishConnection()
    println(res)
    connector.dispose()
    val projectPath =  System.getProperty("user.dir")
    val shellLocation = ShellLocation.HOME.resolve("$projectPath/src/main/")
    shellRun(
        "python",
        listOf(
            "model_runner.py",
            "12347",
            "MOCK",
            "path",
        ), shellLocation
    )
    delay(3000)
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 12347)

    val sendChannel = socket.openWriteChannel(autoFlush = true)
    val receiveChannel = socket.openReadChannel()
    sendChannel.writeStringUtf8("hi")

    var response = receiveChannel.readUTF8Line()
    println(response)

    sendChannel.writeStringUtf8("qwerty aaaaaa")
    response = receiveChannel.readUTF8Line()
    println(response)

    sendChannel.writeStringUtf8("qwert aaaa")
    response = receiveChannel.readUTF8Line()
    println(response)

    sendChannel.writeStringUtf8("by")
    response = receiveChannel.readUTF8Line()
    println(response)

    selectorManager.close()
    socket.close()
}

fun runMockServer(){
    val mockDatabase = MockDatabase(
        bots = mutableListOf(
            BotInfo(
                id = 1, token = BotTokens.GREEK_BOT_TOKEN, actionConfig = BotActionConfig(
                    enabled = true,
                    universalActionType = OnMessageActionType.DELETE,
                    replyText= "Не надо так"
                )
            )
        )
    )
    val testEnvironment = TestEnvironment(
        customBotFactory = TelegramBotFactory(LoggerFactory.getILoggerFactory(), true),
        mockDatabase = mockDatabase
    )
    val runner = AppRunner()
    runner.run(testEnvironment)
}
