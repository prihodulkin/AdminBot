package com.admin_bot.features_test

import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.registration.data.RegisterParams
import com.admin_bot.common.ResponseText
import com.admin_bot.createJsonClient
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.model.authentication.MockJwtAuthenticator
import com.admin_bot.runner.AppTestRunner
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class RegistrationTest: AppTestRunner() {
    @Test
    fun testRegistrationWithEmptyRepository() = testApplication {
        val mockDatabase = MockDatabase()
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterParams(token = "token", password = "Qwerty123"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(MockJwtAuthenticator.authTokens, response.body())
    }

    @Test
    fun testRegistrationWithIncorrectRequestBody() = testApplication {
        val mockDatabase = MockDatabase()
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("error")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ResponseText.incorrectJson, response.bodyAsText())
    }

    @Test
    fun testRegistrationWithDatabaseAccessError() = testApplication {
        val mockDatabase = MockDatabase.withNullValues()
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterParams(token = "token", password = "Qwerty123"))
        }
        assertEquals(HttpStatusCode.LongernalServerError, response.status)
        assertEquals(ResponseText.internalError, response.bodyAsText())
    }

    @Test
    fun testWithIncorrectPassword() = testApplication {
        val mockDatabase = MockDatabase()
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        suspend fun testIncorrectPassword(password: String) {
            var response = client.post("/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterParams(token = "token", password = password))
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals(ResponseText.incorrectPassword, response.bodyAsText())
        }
        withContext(Dispatchers.Default) { testIncorrectPassword("qwerty123") }
        withContext(Dispatchers.Default) { testIncorrectPassword("Qwertyqwerty") }
        withContext(Dispatchers.Default) { testIncorrectPassword("12345678") }
        withContext(Dispatchers.Default) { testIncorrectPassword("Qwerty1") }
    }

    @Test
    fun testRegistrationWithAlreadyUsedToken() = testApplication {
        val mockDatabase = MockDatabase(bots = mutableListOf(BotInfo(1,"token")))
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createJsonClient()
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterParams(token = "token", password = "Qwerty123"))
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
        assertEquals(ResponseText.accessTokenIsAlreadyUsed, response.bodyAsText())
    }
}