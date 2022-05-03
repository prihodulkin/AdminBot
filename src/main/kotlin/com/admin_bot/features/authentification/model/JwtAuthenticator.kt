package com.admin_bot.features.authentification.model

import com.admin_bot.environment.config.ResponseText
import com.admin_bot.environment.config.ServerConfig
import com.admin_bot.features.authentification.data.AuthTokens
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.sql.Date

abstract class JwtAuthenticator {
    companion object {
        const val botIdClaim = "botId"
        const val refreshTokenLength = 100
    }

    open suspend fun generateTokens(serverConfig: ServerConfig, botId: Int): AuthTokens {
        val accessToken = generateAccessToken(serverConfig, botId)
        val refreshToken = generateRefreshToken(serverConfig, botId)
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
                            serverConfig.accessTokenLifetime.inWholeMilliseconds
                )
            )
            .sign(Algorithm.HMAC256(jwtConfig.hs256Secret))
    }

    private fun generateRefreshToken(serverConfig: ServerConfig, botId: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val expiresAt = Clock.System.now().plus(serverConfig.refreshTokenLifetime)
        return "$botId $expiresAt " + (1..refreshTokenLength)
            .map { allowedChars.random() }
            .joinToString("")
    }

    /**
     * Returns Pair(true, botId) if refresh token is valid or Pair(false, null) in the other case.
     * Throws [ExpiredException] if refresh token is expired
     */
    suspend fun validateRefreshTokenAndRemoveOnSuccess(refreshToken: String): Pair<Boolean, Int?> {
        val parsedToken = refreshToken.split(' ')
        val botId = parsedToken.first().toInt()
        if (deleteRefreshTokenIfContains(refreshToken, botId)) {
            checkIsRefreshTokenExpired(parsedToken[1])
            return Pair(true, botId)
        }
        return Pair(false, null)
    }

    /**
     * Throws [ExpiredException] if refresh token is expired
     */
    private fun checkIsRefreshTokenExpired(refreshTokenExpiresDateString: String) {
        val expiresDateTime = Instant.parse(refreshTokenExpiresDateString)
        if (expiresDateTime > Clock.System.now()) {
            throw ExpiredException(ResponseText.refreshTokenIsExpired)
        }
    }

    /**
     * Returns true if [refreshToken] was in storage and was deleted
     */
    protected abstract suspend fun deleteRefreshTokenIfContains(refreshToken: String, botId: Int): Boolean

    protected abstract suspend fun saveRefreshToken(refreshToken: String, botId: Int)

}

class ExpiredException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}