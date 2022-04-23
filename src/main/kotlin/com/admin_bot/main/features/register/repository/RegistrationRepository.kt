package com.admin_bot.main.features.register.repository

import com.admin_bot.main.features.register.data.CompleteRegisterParams
import com.admin_bot.main.features.register.data.EmailConfirmationParams
import com.admin_bot.main.features.register.data.RegisterParams

interface RegistrationRepository {
    suspend fun register(registerParams: RegisterParams):Boolean
    suspend fun sendOtp(completeRegisterParams: CompleteRegisterParams)
    suspend fun verifyOtp(emailConfirmationParams: EmailConfirmationParams):Boolean
}