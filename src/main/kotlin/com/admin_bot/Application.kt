package com.admin_bot

import com.admin_bot.confidential.BotTokens
import com.admin_bot.environment.TestEnvironment
import com.admin_bot.features.bot.model.OnMessageActionType
import com.admin_bot.features.bot_managing.data.BotActionConfig
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.python_classifier.MockPythonClassifierRepository
import com.admin_bot.plugins.telegram_bot.TelegramBotFactory
import com.admin_bot.runner.AppRunner
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory


fun main() = runBlocking {
    runMockServer(withPythonClassifierRepository = true)
}

fun runMockServer(withPythonClassifierRepository: Boolean) {
    val mockDatabase = MockDatabase(
        bots = mutableListOf(
            BotInfo(
                id = 1, token = BotTokens.GREEK_BOT_TOKEN, actionConfig = BotActionConfig(
                    enabled = true,
                    universalActionType = OnMessageActionType.DELETE,
                    replyText = "Не надо так"
                )
            )
        )
    )
    val testEnvironment = TestEnvironment(
        customClassifierRepository =
        if (withPythonClassifierRepository) MockPythonClassifierRepository() else null,
        customBotFactory = TelegramBotFactory(LoggerFactory.getILoggerFactory(), true),
        mockDatabase = mockDatabase

    )
    val runner = AppRunner()
    runner.run(testEnvironment)
}
