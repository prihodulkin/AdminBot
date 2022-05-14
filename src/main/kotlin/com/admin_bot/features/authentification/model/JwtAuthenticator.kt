package com.admin_bot.features.authentification.model

import com.admin_bot.common.errors.AppException
import com.admin_bot.common.errors.ExpiredException
import com.admin_bot.config.ResponseText
import com.admin_bot.config.ServerConfig
import com.admin_bot.features.authentification.data.AuthTokens
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.sql.Date

abstract class JwtAuthenticator(private val refreshTokenStorage: RefreshTokenStorage) {
    companion object {
        const val botIdClaim = "botId"
        const val refreshTokenLength = 100
    }

    open suspend fun generateTokens(serverConfig: ServerConfig, botId: Long): AuthTokens {
        val accessToken = generateAccessToken(serverConfig, botId)
        val refreshToken = generateRefreshToken(serverConfig, botId)
        withContext(Dispatchers.Default) {
            refreshTokenStorage.saveRefreshToken(refreshToken, botId)
        }
        return AuthTokens(accessToken, refreshToken)
    }

    private fun generateAccessToken(serverConfig: ServerConfig, botId: Long): String {
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

    private fun generateRefreshToken(serverConfig: ServerConfig, botId: Long): String {
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
    suspend fun validateRefreshTokenAndRemoveOnSuccess(refreshToken: String): Pair<Boolean, Long?> {
        try {
            val parsedToken = refreshToken.split(' ')
            val botId = parsedToken.first().toLong()
            if (refreshTokenStorage.deleteRefreshTokenIfContains(refreshToken, botId)) {
                checkIsRefreshTokenExpired(parsedToken[1])
                return Pair(true, botId)
            }
            return Pair(false, null)
        } catch (e: ExpiredException) {
            throw e
        } catch (e: Exception) {
            throw RefreshTokenIncorrectFormatException(ResponseText.incorrectRefreshToken)
        }
    }

    /**
     * Throws [ExpiredException] if refresh token is expired
     */
    private fun checkIsRefreshTokenExpired(refreshTokenExpiresDateString: String) {
        val expiresDateTime = Instant.parse(refreshTokenExpiresDateString)
        if (expiresDateTime < Clock.System.now()) {
            throw ExpiredException(HttpStatusCode.Unauthorized, ResponseText.refreshTokenIsExpired)
        }
    }
}

class RefreshTokenIncorrectFormatException(message: String) :
    AppException(HttpStatusCode.Unauthorized, message)

