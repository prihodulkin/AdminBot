package com.admin_bot.features.register.repository

import com.admin_bot.features.register.data.CompleteRegisterParams
import com.admin_bot.features.register.data.EmailConfirmationParams
import com.admin_bot.features.register.data.RegisterParams

interface RegistrationRepository {
    suspend fun register(registerParams: RegisterParams):Boolean
    suspend fun sendOtp(completeRegisterParams: CompleteRegisterParams)
    suspend fun verifyOtp(emailConfirmationParams: EmailConfirmationParams):Boolean
}