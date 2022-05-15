package com.admin_bot.features

import com.admin_bot.environment.TestEnvironment
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.messages.data.Message
import com.admin_bot.plugins.mocks.MockGlobals
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.model.bot.MockDeleteAction
import com.admin_bot.plugins.mocks.model.bot.MockMessageReceiver
import com.admin_bot.runner.AppTestRunner
import io.ktor.server.testing.*
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import org.junit.Test
import kotlin.test.assertEquals

class BotMangingTest : AppTestRunner() {
    private val classifierSubstringValue = MockGlobals.classifierSubstringValue

    @Test
    fun testBotManagerWithSingleBot() = testApplication {
        val mockMessageReceiver = MockMessageReceiver()
        val botInfo = BotInfo(
            id = 1,
            token = "token",
            actionConfig = BotActionConfig(
                enabled = true,
                universalActionType = OnMessageActionType.DELETE
            )
        )
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                botInfo
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123"),
            messageReceivers = mutableMapOf(1L to mockMessageReceiver)
        )
        val testEnvironment = TestEnvironment(mockDatabase)
        val logger = testEnvironment.mockOnMessageActionLogger
        val action = MockDeleteAction(botInfo, logger)
        application {
            runWithTestEnvironment(mockDatabase)
        }
        val goodMessage =
            Message(
                text = "simple text",
                id = 1,
                userId = 1,
                chatId = 1,
                createdAt = Clock.System.now()
            )

        val badMessage =
            Message(
                text = "simple text $classifierSubstringValue",
                id = 1,
                userId = 1,
                chatId = 1,
                createdAt = Clock.System.now()
            )
        mockMessageReceiver.addMessage(goodMessage)
        delay(10L)
        assertEquals(listOf(), logger.loggedItems)

        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action), logger.loggedItems)

        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action, badMessage to action), logger.loggedItems)
    }


}