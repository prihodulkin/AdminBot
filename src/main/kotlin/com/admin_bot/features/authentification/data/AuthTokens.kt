package com.admin_bot.features.authentification.data

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(val accessToken: String, val refreshToken: String)