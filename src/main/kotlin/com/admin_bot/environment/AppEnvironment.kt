package com.admin_bot.environment

import com.admin_bot.config.ServerConfig
import com.admin_bot.features.authentification.model.JwtAuthenticator
import com.admin_bot.features.bot.model.BotFactory
import com.admin_bot.features.bot_managing.model.AdminConfigChangesHandler
import com.admin_bot.features.bot_managing.model.BotInfoRepository
import com.admin_bot.features.bot_managing.model.BotsManager
import com.admin_bot.features.classification.model.ClassifierRepository
import com.admin_bot.features.login.model.LoginRepository
import com.admin_bot.features.registration.model.EmailVerifier
import com.admin_bot.features.registration.model.OtpStorage
import com.admin_bot.features.registration.model.RegistrationManager
import com.admin_bot.features.registration.model.VerificationEmailSender

abstract class AppEnvironment {
    abstract val serverConfig: ServerConfig
    abstract val registrationManager: RegistrationManager
    abstract val loginRepository: LoginRepository
    abstract val jwtAuthenticator: JwtAuthenticator
    abstract val passwordValidator: (String) -> Boolean
    abstract val emailVerifier: EmailVerifier
    abstract val verificationEmailSender: VerificationEmailSender
    abstract val otpStorage: OtpStorage
    abstract val botFactory: BotFactory
    abstract val classifierRepository: ClassifierRepository
    abstract val adminConfigChangesHandler: AdminConfigChangesHandler
    abstract val botInfoRepository: BotInfoRepository
    abstract val botsManager: BotsManager


}