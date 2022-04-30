package com.admin_bot.features.login.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginParams(
    val email: String? = null,
    val token: String? = null,
    val password: String,
)
