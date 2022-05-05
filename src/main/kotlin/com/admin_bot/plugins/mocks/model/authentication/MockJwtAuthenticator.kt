package com.admin_bot.plugins.mocks.model.authentication

import com.admin_bot.config.ServerConfig
import com.admin_bot.features.authentification.data.AuthTokens
import com.admin_bot.features.authentification.model.JwtAuthenticator

class MockJwtAuthenticator(private val useMockAuthTokens: Boolean = true) :
    JwtAuthenticator(
        MockRefreshTokenStorage()
    ) {
    companion object {
        val authTokens = AuthTokens("accessToken", "refreshToken")
    }

    override suspend fun generateTokens(serverConfig: ServerConfig, botId: Int): AuthTokens {
        return if (useMockAuthTokens) authTokens else super.generateTokens(serverConfig, botId)
    }
}