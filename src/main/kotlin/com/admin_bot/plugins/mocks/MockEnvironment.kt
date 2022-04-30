package com.admin_bot.plugins.mocks

import com.admin_bot.environment.AppEnvironment
import com.admin_bot.environment.config.JwtConfig
import com.admin_bot.environment.config.ServerConfig
import com.admin_bot.features.authentification.model.JwtAuthenticator
import com.admin_bot.features.login.model.LoginRepository
import com.admin_bot.features.registration.model.RegistrationRepository
import com.admin_bot.features.helpers.Validators
import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.model.authentication.MockJwtAuthenticator
import com.admin_bot.plugins.mocks.model.login.MockLoginRepository
import com.admin_bot.plugins.mocks.model.registration.MockRegistrationRepository
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class MockEnvironment(
    mockDatabase: MockDatabase,
    val useMockAuthToken: Boolean = true,
    mockServerConfig: ServerConfig? = null
) : AppEnvironment {
    companion object {
        val defaultMockServerConfig = ServerConfig(
            accessTokenLifetime = 1.toDuration(DurationUnit.SECONDS),
            refreshTokenLifetime = 3.toDuration(DurationUnit.SECONDS),
            jwtConfig = JwtConfig(
                hs256Secret = "secret",
                audience = "http://0.0.0.0:8080/",
                issuer = "http://0.0.0.0:8080/"
            )
        )
    }

    override val serverConfig: ServerConfig
    override val passwordValidator = Validators.Companion::validatePassword
    override val registrationRepository: RegistrationRepository
    override val loginRepository: LoginRepository
    override val jwtAuthenticator: JwtAuthenticator = MockJwtAuthenticator()

    init {
        serverConfig = mockServerConfig ?: defaultMockServerConfig
        registrationRepository = MockRegistrationRepository(mockDatabase)
        loginRepository = MockLoginRepository(mockDatabase)
    }
}