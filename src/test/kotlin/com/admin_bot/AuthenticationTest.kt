package com.admin_bot

import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.login.data.LoginParams
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.runner.AppTestRunner
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class AuthenticationTest: AppTestRunner() {
    @Test
    fun testJwtAuth() = testApplication {
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = 1, token = "token")),
            botPasswords = mutableMapOf(1 to "Qwerty123")
        )
        application {
            runWithMockEnvironment(mockDatabase, useMockAuthToken = false)
        }
        val client = createJsonClient()
        client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        var response = client.get("/auth_check")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}