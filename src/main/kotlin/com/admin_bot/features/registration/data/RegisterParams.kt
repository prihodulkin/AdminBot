package com.admin_bot.features.registration.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterParams(val token: String, val password: String)