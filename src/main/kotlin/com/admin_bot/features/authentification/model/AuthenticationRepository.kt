package com.admin_bot.features.authentification.model

import com.admin_bot.environment.config.ServerConfig
import com.admin_bot.features.authentification.data.AuthTokens
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date

abstract class AuthenticationRepository {
    companion object {
        const val botIdClaim = "botId"
        const val refreshTokenLength = 100
    }

    open suspend fun generateTokens(serverConfig: ServerConfig, botId: Int): AuthTokens {
        val accessToken = generateAccessToken(serverConfig, botId)
        val refreshToken = generateRefreshToken(botId)
        withContext(Dispatchers.Default){
            saveRefreshToken(refreshToken, botId)
        }
        return AuthTokens(accessToken, refreshToken)
    }

    private fun generateAccessToken(serverConfig: ServerConfig, botId: Int): String {
        val jwtConfig = serverConfig.jwtConfig
        return JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim(botIdClaim, botId)
            .withExpiresAt(
                Date(
                    System.currentTimeMillis() +
                            serverConfig.accessTokenLife.inWholeMilliseconds
                )
            )
            .sign(Algorithm.HMAC256(jwtConfig.hs256Secret))
    }

    private fun generateRefreshToken(botId: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return "$botId " +(1..refreshTokenLength)
            .map { allowedChars.random() }
            .joinToString("")
    }

    protected abstract suspend fun saveRefreshToken(refreshToken: String, botId: Int)

    abstract suspend fun validateRefreshTokenAndRemoveOnSuccess(refreshToken: String): Boolean
}