package com.admin_bot

import com.admin_bot.environment.config.ResponseText
import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.login.data.LoginParams
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.model.authentication.MockJwtAuthenticator
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.Test
import kotlin.test.assertEquals

class LoginTest {
    @Test
    fun testLoginWithEmptyRepository() = testApplication {
        val mockDatabase = MockDatabase()
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        withContext(Dispatchers.Default) {
            testLoginFailed(
                LoginParams(token = "token", password = "Qwerty123"),
                client
            )
        }
    }

    @Test
    fun testLoginWithCorrectToken() = testApplication {
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = 1, token = "token")),
            botPasswords = mutableMapOf(1 to "Qwerty123")
        )
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(MockJwtAuthenticator.authTokens, response.body())
    }

    @Test
    fun testLoginWithCorrectEmail() = testApplication {
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = 1, adminEmail = "test@test.ru", token = "token")),
            botPasswords = mutableMapOf(1 to "Qwerty123")
        )
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(email = "test@test.ru", password = "Qwerty123"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(MockJwtAuthenticator.authTokens, response.body())
    }

    @Test
    fun testLoginFailedWithIncorrectPassword() = testApplication {
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = 1, adminEmail = "test@test.ru", token = "token")),
            botPasswords = mutableMapOf(1 to "Qwerty123")
        )
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()

        withContext(Dispatchers.Default) {
            testLoginFailed(
                LoginParams(email = "test@test.ru", password = "Qwerty124"),
                client
            )
        }
        withContext(Dispatchers.Default) {
            testLoginFailed(
                LoginParams(token = "token", password = "Qwerty124"),
                client
            )
        }
    }

    @Test
    fun testLoginFailedWithNonExistentEmailOrToken() = testApplication {
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = 1, adminEmail = "test@test.ru", token = "token")),
            botPasswords = mutableMapOf(1 to "Qwerty123")
        )
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()

        withContext(Dispatchers.Default) {
            testLoginFailed(
                LoginParams(email = "test@tesy.ru", password = "Qwerty124"),
                client
            )
        }
        withContext(Dispatchers.Default) {
            testLoginFailed(
                LoginParams(token = "tokem", password = "Qwerty124"),
                client
            )
        }
    }

    @Test
    fun testLoginFailedWithIncorrectParams() = testApplication {
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = 1, token = "token")),
            botPasswords = mutableMapOf(1 to "Qwerty123")
        )
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        suspend fun testLoginFailedWithBadRequest(loginParams: LoginParams, expectedResponseText: String) {
            var response = client.post("/login") {
                contentType(ContentType.Application.Json)
                setBody(loginParams)
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals(expectedResponseText, response.bodyAsText())
        }
        withContext(Dispatchers.Default) {
            testLoginFailedWithBadRequest(
                LoginParams(password = "Qwerty123"),
                expectedResponseText = ResponseText.useEmailOrAccessTokenForLogin
            )
        }
        withContext(Dispatchers.Default) {
            testLoginFailedWithBadRequest(
                LoginParams(password = "Qwerty123", email = "test@test.ru", token = "token"),
                expectedResponseText = ResponseText.useOnlyEmailOrAccessTokenForLogin
            )
        }
    }

    @Test
    fun testLoginFailedWithDatabaseError() = testApplication {
        val mockDatabase = MockDatabase.withNullValues()
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertEquals(ResponseText.internalError, response.bodyAsText())
    }


    private suspend fun testLoginFailed(loginParams: LoginParams, client: HttpClient) {
        var response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(loginParams)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(ResponseText.loginFailed, response.bodyAsText())
    }

}