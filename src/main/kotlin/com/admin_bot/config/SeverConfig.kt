package com.admin_bot.config

import kotlin.time.Duration

data class ServerConfig(
    val accessTokenLifetime: Duration,
    val refreshTokenLifetime: Duration,
    val jwtConfig: JwtConfig
)

data class JwtConfig(
    val hs256Secret: String,
    val audience: String,
    val issuer: String
)