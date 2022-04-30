package com.admin_bot.plugins.mocks.model.authentication

import com.admin_bot.environment.config.ServerConfig
import com.admin_bot.features.authentification.data.AuthTokens
import com.admin_bot.features.authentification.model.JwtAuthenticator

class MockJwtAuthenticator(private val mockAuthTokens: Boolean = true) : JwtAuthenticator() {
    companion object {
        val authTokens = AuthTokens("accessToken", "refreshToken")
    }

    private val refreshTokens: MutableMap<Int, MutableSet<String>> = mutableMapOf()

    override suspend fun generateTokens(serverConfig: ServerConfig, botId: Int): AuthTokens {
        return if (mockAuthTokens) authTokens else super.generateTokens(serverConfig, botId)
    }

    override suspend fun saveRefreshToken(refreshToken: String, botId: Int) {
        if (refreshTokens.containsKey(botId)) {
            refreshTokens[botId]!!.add(refreshToken)
        } else {
            refreshTokens[botId] = mutableSetOf(refreshToken)
        }
    }

    override suspend fun validateRefreshTokenAndRemoveOnSuccess(refreshToken: String): Boolean {
        val botId = refreshToken.split(' ').first().toInt()
        val refreshTokensSet = refreshTokens[botId]
        if (refreshTokensSet?.contains(refreshToken) == true) {
            refreshTokensSet.remove(refreshToken)
            return true
        }
        return false
    }
}