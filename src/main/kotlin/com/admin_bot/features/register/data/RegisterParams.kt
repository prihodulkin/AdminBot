package com.admin_bot.features.register.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterParams(val accessToken: String, val password: String)