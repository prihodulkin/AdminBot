package com.admin_bot.environment

import com.admin_bot.config.JwtConfig
import com.admin_bot.config.ServerConfig
import com.admin_bot.features.authentification.model.JwtAuthenticator
import com.admin_bot.common.Validators
import com.admin_bot.features.login.model.LoginRepository
import com.admin_bot.features.registration.model.EmailVerifier
import com.admin_bot.features.registration.model.OtpStorage
import com.admin_bot.features.registration.model.RegistrationManager
import com.admin_bot.features.registration.model.VerificationEmailSender
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.model.authentication.MockJwtAuthenticator
import com.admin_bot.plugins.mocks.model.login.MockLoginRepository
import com.admin_bot.plugins.mocks.model.registration.MockOtpStorage
import com.admin_bot.plugins.mocks.model.registration.MockRegistrationManager
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class TestEnvironment(
    mockDatabase: MockDatabase,
    val useMockAuthTokens: Boolean = true,
    mockServerConfig: ServerConfig? = null
) : AppEnvironment {
    companion object {
        val defaultMockServerConfig = ServerConfig(
            accessTokenLifetime = 1.toDuration(DurationUnit.SECONDS),
            refreshTokenLifetime = 3.toDuration(DurationUnit.SECONDS),
            otpLifetime =   3.toDuration(DurationUnit.SECONDS),
            jwtConfig = JwtConfig(
                hs256Secret = "secret",
                audience = "http://0.0.0.0:8080/",
                issuer = "http://0.0.0.0:8080/"
            ),
            smtpHostname = "bot_admin",
            otpLength = 6,
        )
    }

    override val serverConfig: ServerConfig
    override val passwordValidator = Validators.Companion::validatePassword
    override val registrationManager: RegistrationManager
    override val loginRepository: LoginRepository
    override val jwtAuthenticator: JwtAuthenticator
    override val verificationEmailSender: VerificationEmailSender
    override val emailVerifier: EmailVerifier
    override val otpStorage: OtpStorage

    init {
        serverConfig = mockServerConfig ?: defaultMockServerConfig
        registrationManager = MockRegistrationManager(mockDatabase)
        loginRepository = MockLoginRepository(mockDatabase)
        jwtAuthenticator = MockJwtAuthenticator(useMockAuthTokens)
        verificationEmailSender = VerificationEmailSender(serverConfig)
        otpStorage = MockOtpStorage(mockDatabase)
        emailVerifier = EmailVerifier(verificationEmailSender, otpStorage, serverConfig)
    }
}