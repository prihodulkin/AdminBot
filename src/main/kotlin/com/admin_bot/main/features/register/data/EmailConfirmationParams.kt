package com.admin_bot.main.features.register.data

import kotlinx.serialization.Serializable

@Serializable
data class EmailConfirmationParams(val otp: String)