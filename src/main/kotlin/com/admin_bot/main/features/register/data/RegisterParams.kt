package com.admin_bot.main.features.register.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterParams(val accessToken: String, val password: String)