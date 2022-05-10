package com.admin_bot.config

import kotlin.time.Duration

data class ServerConfig(
    val accessTokenLifetime: Duration,
    val refreshTokenLifetime: Duration,
    val otpLifetime: Duration,
    val jwtConfig: JwtConfig,
    val smtpHostname: String,
    val otpLength: Long
)

data class JwtConfig(
    val hs256Secret: String,
    val audience: String,
    val issuer: String
)