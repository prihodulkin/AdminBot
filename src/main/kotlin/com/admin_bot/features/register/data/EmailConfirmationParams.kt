package com.admin_bot.features.register.data

import kotlinx.serialization.Serializable

@Serializable
data class EmailConfirmationParams(val otp: String)