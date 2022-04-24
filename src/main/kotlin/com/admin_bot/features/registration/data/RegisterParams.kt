package com.admin_bot.features.registration.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterParams(val accessToken: String, val password: String)