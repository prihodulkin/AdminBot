package com.admin_bot.features.registration.data

import kotlinx.serialization.Serializable

@Serializable
data class JwtRefreshParams(val refreshToken: String)