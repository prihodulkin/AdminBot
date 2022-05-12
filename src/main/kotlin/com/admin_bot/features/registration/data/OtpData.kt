package com.admin_bot.features.registration.data

import kotlinx.datetime.Instant

data class OtpData(val otpValue: String, val email: String, val expiresAt: Instant)