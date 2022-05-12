package com.admin_bot.features.registration.model

import com.admin_bot.common.ExpiredException
import com.admin_bot.common.ResponseText
import com.admin_bot.config.ServerConfig
import com.admin_bot.features.registration.data.OtpData
import io.ktor.http.*
import kotlinx.datetime.Clock

/**
 * Generates and sends OTP and verifies admin email
 */
class EmailVerifier(
    private val verificationEmailSender: VerificationEmailSender,
    private val otpStorage: OtpStorage,
    private val serverConfig: ServerConfig
) {

    suspend fun generateAndSendOtp(botId: Long, email: String) {
        val otp = generateOtp()
        val expiresAt = Clock.System.now().plus(serverConfig.otpLifetime)
        otpStorage.saveOtpData(botId, OtpData(otp, email, expiresAt))
        verificationEmailSender.sendMailWithOtp(email, expiresAt, otp)
    }

    /**
     * Returns confirmed email if otp was verified successfully and null in the other case
     */
    suspend fun verifyOtp(botId: Long, otp: String): String? {
        val otpData = otpStorage.getOtpData(botId)
        if (otpData != null && otpData.otpValue == otp) {
            if (otpData.expiresAt < Clock.System.now()) {
                throw ExpiredException(HttpStatusCode.BadRequest, ResponseText.otpWasExpired)
            }
            otpStorage.deleteOtpData(botId)
            return otpData.email
        }
        return null
    }


    private fun generateOtp(): String {
        return (1..serverConfig.otpLength).map { ('0'..'9').random() }.joinToString("")
    }
}