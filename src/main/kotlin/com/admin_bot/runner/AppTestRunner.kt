package com.admin_bot.runner

import com.admin_bot.environment.AppEnvironment
import com.admin_bot.config.ServerConfig
import com.admin_bot.environment.TestEnvironment
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.route.authCheckRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

open class AppTestRunner : AppRunner() {
    fun Application.runWithMockEnvironment(
        mockDatabase: MockDatabase,
        useMockAuthToken: Boolean = true,
        mockServerConfig: ServerConfig? = null
    ) {
        val testEnvironment =
            TestEnvironment(
                mockDatabase,
                useMockAuthTokens = useMockAuthToken,
                mockServerConfig = mockServerConfig
            )
        run(testEnvironment)
    }

    override fun Route.configureRouting(appEnvironment: AppEnvironment) {
        configureBaseRouting(appEnvironment)
        authCheckRoute()
    }
}