package com.admin_bot.features.authentification.data

import kotlinx.serialization.Serializable

@Serializable
data class JwtRefreshParams(val refreshToken: String)