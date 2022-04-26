package com.admin_bot.environment

import com.admin_bot.features.login.repository.LoginRepository
import com.admin_bot.features.registration.repository.RegistrationRepository

interface AppEnvironment{
    val registrationRepository: RegistrationRepository
    val loginRepository: LoginRepository
}