package com.admin_bot.environment

import com.admin_bot.environment.config.ServerConfig
import com.admin_bot.features.authentification.model.AuthenticationRepository
import com.admin_bot.features.login.model.LoginRepository
import com.admin_bot.features.registration.model.RegistrationRepository

interface AppEnvironment{
    val serverConfig: ServerConfig
    val registrationRepository: RegistrationRepository
    val loginRepository: LoginRepository
    val authenticationRepository: AuthenticationRepository
    val passwordValidator: (String)->Boolean
}