package com.admin_bot.environment

import com.admin_bot.environment.config.ServerConfig
import com.admin_bot.features.authentification.model.JwtAuthenticator
import com.admin_bot.features.login.model.LoginRepository
import com.admin_bot.features.registration.model.RegistrationRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

interface AppEnvironment{
    val serverConfig: ServerConfig
    val registrationRepository: RegistrationRepository
    val loginRepository: LoginRepository
    val jwtAuthenticator: JwtAuthenticator
    val passwordValidator: (String) -> Boolean
}