package com.admin_bot.plugins.mocks.model.authentication

import com.admin_bot.features.authentification.model.RefreshTokenStorage

class MockRefreshTokenStorage: RefreshTokenStorage {
    private val refreshTokens: MutableMap<Long, MutableSet<String>> = mutableMapOf()

    override suspend fun saveRefreshToken(refreshToken: String, botId: Long) {
        if (refreshTokens.containsKey(botId)) {
            refreshTokens[botId]!!.add(refreshToken)
        } else {
            refreshTokens[botId] = mutableSetOf(refreshToken)
        }
    }

    override suspend fun deleteRefreshTokenIfContains(refreshToken: String, botId: Long): Boolean {
        val refreshTokensSet = refreshTokens[botId]
        if (refreshTokensSet?.contains(refreshToken) == true) {
            refreshTokensSet.remove(refreshToken)
            return true
        }
        return false
    }
}