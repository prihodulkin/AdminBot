package com.admin_bot.features.registration.model

import com.admin_bot.config.ServerConfig
import kotlinx.datetime.Instant
import net.axay.simplekotlinmail.delivery.mailerBuilder
import net.axay.simplekotlinmail.email.emailBuilder
import org.simplejavamail.api.mailer.Mailer

open class VerificationEmailSender(serverConfig: ServerConfig) {
    private val mailer: Mailer

    init {
        mailer = mailerBuilder(host = serverConfig.smtpHostname, port = 25)
    }

    open fun sendMailWithOtp(emailAddress: String, expiresAt: Instant, code: String) {
        mailer.sendMail(emailBuilder {
            to(emailAddress)
            withSubject("Bot Admin email verification")
            withPlainText("Your email verification code: $code. You can use it until $expiresAt.")
        })
    }
}

