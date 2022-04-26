package com.admin_bot

import com.admin_bot.features.registration.data.RegisterParams
import com.admin_bot.plugins.mocks.database.MockDatabase
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRegistrationWithEmptyRepository() = testApplication {
        val mockDatabase = MockDatabase()
        application {
            runWithMockEnvironment(mockDatabase)
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterParams(accessToken = "token", password = "Qwerty123"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }
}