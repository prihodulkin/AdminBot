package com.admin_bot.features.registration.repository

import com.admin_bot.features.registration.data.CompleteRegisterParams
import com.admin_bot.features.registration.data.EmailConfirmationParams
import com.admin_bot.features.registration.data.RegisterParams

interface RegistrationRepository {
    suspend fun register(registerParams: RegisterParams):Boolean
    suspend fun sendOtp(completeRegisterParams: CompleteRegisterParams)
    suspend fun verifyOtp(emailConfirmationParams: EmailConfirmationParams):Boolean
}