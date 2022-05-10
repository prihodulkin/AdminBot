package com.admin_bot.features.authentification.model

interface RefreshTokenStorage {
    /**
     * Returns true if [refreshToken] was in storage and was deleted
     */
     suspend fun deleteRefreshTokenIfContains(refreshToken: String, botId: Long): Boolean

     suspend fun saveRefreshToken(refreshToken: String, botId: Long)
}