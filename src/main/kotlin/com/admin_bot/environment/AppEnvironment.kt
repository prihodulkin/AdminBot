package com.admin_bot.environment

import com.admin_bot.config.ServerConfig
import com.admin_bot.features.authentification.model.JwtAuthenticator
import com.admin_bot.features.login.model.LoginRepository
import com.admin_bot.features.registration.model.RegistrationManager

interface AppEnvironment{
    val serverConfig: ServerConfig
    val registrationManager: RegistrationManager
    val loginRepository: LoginRepository
    val jwtAuthenticator: JwtAuthenticator
    val passwordValidator: (String) -> Boolean
}