package com.admin_bot.features

import com.admin_bot.config.ResponseText
import com.admin_bot.createJsonClient
import com.admin_bot.features.authentification.data.AuthTokens
import com.admin_bot.features.authentification.data.JwtRefreshParams
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.login.data.LoginParams
import com.admin_bot.environment.TestEnvironment
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.runner.AppTestRunner
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.delay
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AuthenticationTest: AppTestRunner() {
    private val accessTokenExpiresDuration: Duration =
        TestEnvironment.defaultMockServerConfig.accessTokenLifetime.plus(1.toDuration(DurationUnit.SECONDS))
    private val refreshTokenExpiresDuration: Duration =
        TestEnvironment.defaultMockServerConfig.refreshTokenLifetime.plus(1.toDuration(DurationUnit.SECONDS))
    private val mockDatabase = MockDatabase(
        bots = mutableListOf(BotInfo(id = 1, token = "token")),
        botPasswords = mutableMapOf(1L to "Qwerty123")
    )

    @Test
    fun testJwtAccessToken() = testApplication {
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        var response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        val authTokens = response.body<AuthTokens>()
        val accessToken = authTokens.accessToken
        testAuthorizationSucceed(client, accessToken)
        delay(accessTokenExpiresDuration)
        testAuthorizationFailed(client, accessToken)
    }

    @Test
    fun testRefreshToken() = testApplication {
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        var response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        val authTokens = response.body<AuthTokens>()
        val accessToken = authTokens.accessToken
        val refreshToken = authTokens.refreshToken
        delay(accessTokenExpiresDuration)
        testAuthorizationFailed(client, accessToken)
        response = client.post("jwt_refresh") {
            contentType(ContentType.Application.Json)
            setBody(JwtRefreshParams(refreshToken))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val newAccessToken = response.body<AuthTokens>().accessToken
        testAuthorizationSucceed(client, newAccessToken)
    }

    @Test
    fun testIncorrectRefreshToken() = testApplication {
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        var response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        val authTokens = response.body<AuthTokens>()
        val accessToken = authTokens.accessToken
        delay(accessTokenExpiresDuration)
        testAuthorizationFailed(client, accessToken)
        testRefreshTokenUsageFailed(client, "incorrectRefreshToken&")
    }

    @Test
    fun testExpiredRefreshToken() = testApplication {
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        var response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        val authTokens = response.body<AuthTokens>()
        val refreshToken = authTokens.refreshToken
        val accessToken = authTokens.accessToken
        delay(refreshTokenExpiresDuration)
        testAuthorizationFailed(client, accessToken)
        testRefreshTokenUsageFailed(client, refreshToken, responseText = ResponseText.refreshTokenIsExpired)
    }

    @Test
    fun testRefreshTokenIsDisposable() = testApplication {
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        var response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginParams(token = "token", password = "Qwerty123"))
        }
        val authTokens = response.body<AuthTokens>()
        val refreshToken = authTokens.refreshToken
        response = client.post("jwt_refresh") {
            contentType(ContentType.Application.Json)
            setBody(JwtRefreshParams(refreshToken))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        testRefreshTokenUsageFailed(
            client,
            refreshToken,
            responseText = ResponseText.incorrectRefreshToken
        )
    }


    private suspend fun testAuthorizationSucceed(client: HttpClient, accessToken: String) {
        val response = client.get("/auth_check") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    private suspend fun testAuthorizationFailed(client: HttpClient, accessToken: String) {
        val response = client.get("/auth_check") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(ResponseText.loginFailed, response.bodyAsText())
    }

    private suspend fun testRefreshTokenUsageFailed(
        client: HttpClient,
        refreshToken: String,
        statusCode: HttpStatusCode = HttpStatusCode.Unauthorized,
        responseText: String = ResponseText.incorrectRefreshToken
    ) {
        val response = client.post("jwt_refresh") {
            contentType(ContentType.Application.Json)
            setBody(JwtRefreshParams(refreshToken))
        }
        assertEquals(statusCode, response.status)
        assertEquals(responseText, response.bodyAsText())
    }
}