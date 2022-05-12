package com.admin_bot.plugins.mocks.model.registration;

import com.admin_bot.config.ServerConfig;
import com.admin_bot.features.registration.model.VerificationEmailSender;
import kotlinx.datetime.Instant

class MockVerificationEmailSender(serverConfig: ServerConfig) : VerificationEmailSender(serverConfig) {
    var lastEmail: String? = null
    override fun sendMailWithOtp(emailAddress: String, expiresAt: Instant, code: String) {
        lastEmail=emailAddress
    }
}
