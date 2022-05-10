package com.admin_bot.features.registration.model

import com.admin_bot.config.ServerConfig
import com.admin_bot.features.registration.data.OtpData
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
        otpStorage.saveOtpData(botId, OtpData(otp, expiresAt))
        verificationEmailSender.sendMailWithOtp(email, expiresAt, otp)
    }

    suspend fun verifyOtp(botId: Long, otp: String):Boolean{
        val otpData = otpStorage.getOtpData(botId)
        if(otpData!=null&&otpData.otpValue==otp){
            otpStorage.deleteOtpData(botId)
            return true
        }
        return false
    }


    private fun generateOtp(): String {
        return (1..serverConfig.otpLength).map { ('0'..'9').random() }.joinToString("")
    }
}