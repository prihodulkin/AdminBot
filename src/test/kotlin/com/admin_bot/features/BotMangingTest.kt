package com.admin_bot.features

import com.admin_bot.environment.TestEnvironment
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotActionConfigChange
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.messages.data.Message
import com.admin_bot.plugins.mocks.MockGlobals
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.model.bot.MockBanAction
import com.admin_bot.plugins.mocks.model.bot.MockDeleteAction
import com.admin_bot.plugins.mocks.model.bot.MockMessageReceiver
import com.admin_bot.runner.AppTestRunner
import io.ktor.server.testing.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.junit.Test
import kotlin.random.Random.Default.nextLong
import kotlin.test.assertEquals

class BotMangingTest : AppTestRunner() {
    private val classifierSubstringValue = MockGlobals.classifierSubstringValue

    private val goodMessage =
        Message(
            text = "simple text",
            id = 1,
            userId = 1,
            chatId = 1,
            createdAt = Clock.System.now()
        )

    private val badMessage =
        Message(
            text = "simple text $classifierSubstringValue",
            id = 1,
            userId = 1,
            chatId = 1,
            createdAt = Clock.System.now()
        )

    private val firstBot = BotInfo(
        id = 1,
        token = "token",
        actionConfig = BotActionConfig(
            enabled = true,
            universalActionType = OnMessageActionType.DELETE
        )
    )

    @Test
    fun testBotManagerMessagesHandlingWithSingleBot() = testApplication {
        val mockMessageReceiver = MockMessageReceiver()
        val botInfo = firstBot
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
        // add good message and check that delete action wasn't executed
        delay(10L)
        mockMessageReceiver.addMessage(goodMessage)
        delay(10L)
        assertEquals(listOf(), logger.loggedItems)

        // add bad message and check that delete action was executed
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action), logger.loggedItems)

        // add bad message and check that delete action was executed
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action, badMessage to action), logger.loggedItems)

        // add good message and check that delete action wasn't executed
        mockMessageReceiver.addMessage(goodMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action, badMessage to action), logger.loggedItems)
    }

    @Test
    fun testBotManagerBotDisablingWithSingleBot() = testApplication {
        val mockMessageReceiver = MockMessageReceiver()
        val botInfo = firstBot
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
        // add bad message and check that delete action was executed
        delay(10L)
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action), logger.loggedItems)

        //disable bot and check that messages were not handled
        val configChangesHandler = testEnvironment.adminConfigChangesHandler
        configChangesHandler.changeConfig(1L, BotActionConfigChange(enabled = false))
        delay(10L)
        mockMessageReceiver.addMessage(badMessage)
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action), logger.loggedItems)

        //disable bot again and check that messages were not handled
        configChangesHandler.changeConfig(1L, BotActionConfigChange(enabled = false))
        delay(10L)
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action), logger.loggedItems)

        //enable bot and check that messages will be handled
        configChangesHandler.changeConfig(1L, BotActionConfigChange(enabled = true))
        delay(10L)
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to action, badMessage to action), logger.loggedItems)
    }

    @Test
    fun testBotManagerActionChangingWithSingleBot() = testApplication {
        val mockMessageReceiver = MockMessageReceiver()
        val botInfo = firstBot
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                botInfo
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123"),
            messageReceivers = mutableMapOf(1L to mockMessageReceiver)
        )
        val testEnvironment = TestEnvironment(mockDatabase)
        val logger = testEnvironment.mockOnMessageActionLogger
        val deleteAction = MockDeleteAction(botInfo, logger)
        application {
            runWithTestEnvironment(mockDatabase)
        }

        // add bad message and check that delete action was executed
        delay(10L)
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(listOf(badMessage to deleteAction), logger.loggedItems)

        // change action to ban
        val configChangesHandler = testEnvironment.adminConfigChangesHandler
        val banAction = MockBanAction(botInfo, logger)
        configChangesHandler.changeConfig(
            1L,
            BotActionConfigChange(universalActionType = banAction.type)
        )
        delay(10L)

        // check that action was changed to ban
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(badMessage to banAction, logger.loggedItems.last())

        // change action to reply
        val replyAction = MockBanAction(botInfo, logger)
        configChangesHandler.changeConfig(
            1L,
            BotActionConfigChange(universalActionType = replyAction.type)
        )
        delay(10L)

        // check that action changed to reply
        mockMessageReceiver.addMessage(badMessage)
        delay(10L)
        assertEquals(badMessage to replyAction, logger.loggedItems.last())
    }

    @Test
    fun testBotManagerActionChangingForDifferentChatsWithSingleBot() = testApplication {
        val mockMessageReceiver = MockMessageReceiver()
        val botInfo = firstBot
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                botInfo
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123"),
            messageReceivers = mutableMapOf(1L to mockMessageReceiver)
        )
        val testEnvironment = TestEnvironment(mockDatabase)
        val logger = testEnvironment.mockOnMessageActionLogger
        val deleteAction = MockDeleteAction(botInfo, logger)
        application {
            runWithTestEnvironment(mockDatabase)
        }
        val badMessageChat1 =
            Message(
                text = "simple text $classifierSubstringValue",
                id = 1,
                userId = 1,
                chatId = 1,
                createdAt = Clock.System.now()
            )
        val badMessageChat2 =
            Message(
                text = "simple text $classifierSubstringValue",
                id = 1,
                userId = 1,
                chatId = 2,
                createdAt = Clock.System.now()
            )

        val loggedItems = logger.loggedItems

        // add bad message from chat 1 and check that delete action was executed
        delay(10L)
        mockMessageReceiver.addMessage(badMessageChat1)
        delay(10L)
        assertEquals(listOf(badMessageChat1 to deleteAction), loggedItems)

        // add bad message from chat 2 and check that delete action was executed
        mockMessageReceiver.addMessage(badMessageChat2)
        delay(10L)
        assertEquals(listOf(badMessageChat1 to deleteAction, badMessageChat2 to deleteAction), loggedItems)

        // change action for chat 2 to ban
        val configChangesHandler = testEnvironment.adminConfigChangesHandler
        val banAction = MockBanAction(botInfo, logger)
        configChangesHandler.changeConfig(
            1L,
            BotActionConfigChange(chatsActionTypes = mutableMapOf(2L to banAction.type))
        )
        delay(10L)
        // universal action type still != NONE and nothing should be changed
        mockMessageReceiver.addMessage(badMessageChat1)
        delay(10L)
        mockMessageReceiver.addMessage(badMessageChat2)
        delay(10L)
        assertEquals(
            listOf(
                badMessageChat1 to deleteAction,
                badMessageChat2 to deleteAction,
                badMessageChat1 to deleteAction,
                badMessageChat2 to deleteAction,
            ), loggedItems
        )

        // set universal action type to NONE
        configChangesHandler.changeConfig(
            1L,
            BotActionConfigChange(universalActionType = OnMessageActionType.NONE)
        )

        // for chat 2 ban action should be executed
        mockMessageReceiver.addMessage(badMessageChat1)
        delay(10L)
        mockMessageReceiver.addMessage(badMessageChat2)
        delay(10L)
        assertEquals(badMessageChat1 to deleteAction, loggedItems[loggedItems.size - 2])
        assertEquals(badMessageChat2 to banAction, loggedItems.last())
    }

    @Test
    fun testBotManagerLoadWithSingleBot() = testApplication {
        val mockMessageReceiver = MockMessageReceiver()
        val botInfo = firstBot
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                botInfo
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123"),
            messageReceivers = mutableMapOf(1L to mockMessageReceiver)
        )
        val testEnvironment = TestEnvironment(mockDatabase)
        val logger = testEnvironment.mockOnMessageActionLogger
        application {
            runWithTestEnvironment(mockDatabase)
        }
        delay(10)
        coroutineScope {
            for (i in 1..200) {
                delay(1)
                launch {
                    mockMessageReceiver.addMessage(badMessage)
                }
            }
        }
        delay(100)
        assertEquals(200, logger.loggedItems.size)
    }

}
