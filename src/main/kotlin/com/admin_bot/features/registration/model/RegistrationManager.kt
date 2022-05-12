package com.admin_bot.features.registration.model

import com.admin_bot.features.registration.data.RegisterParams

interface RegistrationManager {
    suspend fun register(registerParams: RegisterParams):Long?
    suspend fun isEmailNotBusy(email: String): Boolean
    suspend fun completeRegistration(botId: Long, email: String)
    suspend fun unregister(botId: Long): Boolean
}