package com.admin_bot.features.registration.repository

import com.admin_bot.features.registration.data.CompleteRegisterParams
import com.admin_bot.features.registration.data.EmailConfirmationParams
import com.admin_bot.features.registration.data.RegisterParams

interface RegistrationRepository {
    suspend fun register(registerParams: RegisterParams):Int?
    suspend fun sendOtp(completeRegisterParams: CompleteRegisterParams, botId: Int)
    suspend fun verifyOtp(emailConfirmationParams: EmailConfirmationParams, botId: Int):Boolean
    suspend fun unregister(botId: Int):Boolean
}