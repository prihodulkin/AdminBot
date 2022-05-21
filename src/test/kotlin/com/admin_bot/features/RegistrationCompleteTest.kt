package com.admin_bot.features

import com.admin_bot.config.ResponseText
import com.admin_bot.createJsonClient
import com.admin_bot.environment.TestEnvironment
import com.admin_bot.features.authentification.data.AuthTokens
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.login.data.LoginParams
import com.admin_bot.features.registration.data.CompleteRegistrationParams
import com.admin_bot.features.registration.data.EmailConfirmationParams
import com.admin_bot.features.registration.data.OtpData
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.model.registration.MockVerificationEmailSender
import com.admin_bot.runner.AppTestRunner
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.Clock
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RegistrationCompleteTest : AppTestRunner() {
    @Test
    fun testOtpSendingAndSaving() = testApplication {
        val botId = 1L
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = botId, token = "token")),
            botPasswords = mutableMapOf(1L to "Qwerty123")
        )
        val testEnvironment = TestEnvironment(mockDatabase, useMockAuthTokens = false)
        val emailSender = testEnvironment.verificationEmailSender as MockVerificationEmailSender
        assertEquals(null, emailSender.lastEmail)
        assert(!(mockDatabase.oneTimePasswords?.containsKey(botId) ?: false))
        application {
            run(testEnvironment)
        }
        val client = createJsonClient()
        val accessToken = getAccessToken(client, LoginParams(token = "token", password = "Qwerty123"))
        val email = "test@test.ru"
        val response = client.post("/register/complete") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            setBody(CompleteRegistrationParams(email))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ResponseText.otpWasSent(email), response.bodyAsText())
        assertEquals(email, emailSender.lastEmail)
        assert(mockDatabase.oneTimePasswords?.containsKey(botId) ?: false)
    }

    @Test
    fun testOtpRequestFailedWithIncorrectEmail() = testApplication {
        val mockDatabase = MockDatabase(
            bots = mutableListOf(BotInfo(id = 1, token = "token")),
            botPasswords = mutableMapOf(1L to "Qwerty123")
        )
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        val accessToken = getAccessToken(client, LoginParams(token = "token", password = "Qwerty123"))
        val email = "test|test.ru"
        val response = client.post("/register/complete") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            setBody(CompleteRegistrationParams(email))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ResponseText.incorrectEmailFormat, response.bodyAsText())
    }


    @Test
    fun testOtpRequestFailedWithAlreadyUsedEmail() = testApplication {
        val email = "test@test.ru"
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                BotInfo(id = 1, token = "token1"),
                BotInfo(id = 2, token = "token2", adminEmail = email)
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123")
        )
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        val accessToken = getAccessToken(client, LoginParams(token = "token1", password = "Qwerty123"))
        val response = client.post("/register/complete") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            setBody(CompleteRegistrationParams(email))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ResponseText.emailIsAlreadyUsed, response.bodyAsText())
    }

    @Test
    fun testOtpVerificationSuccess() = testApplication {
        val email = "test@test.ru"
        val otp = "111111"
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                BotInfo(id = 1, token = "token"),
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123"),
            oneTimePasswords = mutableMapOf(
                1L to OtpData(
                    otp,
                    email,
                    Clock.System.now().plus(10.toDuration(DurationUnit.SECONDS))
                )
            )

        )
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        val accessToken = getAccessToken(client, LoginParams(token = "token", password = "Qwerty123"))
        val response = client.post("/register/otp") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            setBody(EmailConfirmationParams(otp))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ResponseText.emailWasConfirmed, response.bodyAsText())
        assert(mockDatabase.bots!!.firstOrNull { bot -> bot.adminEmail == email } != null)
    }

    @Test
    fun testOtpVerificationFailedWithExpiredOtp() = testApplication {
        val email = "test@test.ru"
        val otp = "111111"
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                BotInfo(id = 1, token = "token"),
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123"),
            oneTimePasswords = mutableMapOf(
                1L to OtpData(
                    otp,
                    email,
                    Clock.System.now().minus(10.toDuration(DurationUnit.SECONDS))
                )
            )

        )
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        val accessToken = getAccessToken(client, LoginParams(token = "token", password = "Qwerty123"))
        val response = client.post("/register/otp") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            setBody(EmailConfirmationParams(otp))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ResponseText.otpWasExpired, response.bodyAsText())
    }

    @Test
    fun testOtpVerificationFailedWithWrongOtp() = testApplication {
        val email = "test@test.ru"
        val mockDatabase = MockDatabase(
            bots = mutableListOf(
                BotInfo(id = 1, token = "token1"),
                BotInfo(id = 2, token = "token2", adminEmail = email)
            ),
            botPasswords = mutableMapOf(1L to "Qwerty123"),
            oneTimePasswords = mutableMapOf(
                1L to OtpData(
                    "111111",
                    email,
                    Clock.System.now().minus(10.toDuration(DurationUnit.SECONDS))
                )
            )
        )
        application {
            runWithTestEnvironment(mockDatabase, useMockAuthTokens = false)
        }
        val client = createJsonClient()
        val accessToken = getAccessToken(client, LoginParams(token = "token1", password = "Qwerty123"))
        val response = client.post("/register/otp") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            setBody(EmailConfirmationParams("222222"))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(ResponseText.incorrectOtp, response.bodyAsText())
    }
}

suspend fun getAccessToken(client: HttpClient, loginParams: LoginParams): String {
    return client.post("/login") {
        contentType(ContentType.Application.Json)
        setBody(loginParams)
    }.body<AuthTokens>().accessToken
}

