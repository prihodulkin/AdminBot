package com.admin_bot.plugins.mocks.model.registration

import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.plugins.mocks.MockGlobals
import com.admin_bot.features.bot.data.BotInfo
import com.admin_bot.features.registration.data.CompleteRegisterParams
import com.admin_bot.features.registration.data.EmailConfirmationParams
import com.admin_bot.features.registration.data.RegisterParams
import com.admin_bot.features.registration.model.RegistrationRepository

class MockRegistrationRepository(private val mockDatabase: MockDatabase) : RegistrationRepository {
    override suspend fun register(registerParams: RegisterParams): Int? {
        val bots = mockDatabase!!.bots!!
        val botInfo = bots.firstOrNull { bot -> bot.token == registerParams.token }
        if (botInfo != null) {
            return null
        }
        val id = bots.size
        bots.add(BotInfo(id = id, token = registerParams.token))
        return id
    }

    override suspend fun sendOtp(completeRegisterParams: CompleteRegisterParams, botId: Int) {
        val otp = mockDatabase.oneTimePasswords!!
        otp[botId] = MockGlobals.otp.toString()
    }

    override suspend fun verifyOtp(emailConfirmationParams: EmailConfirmationParams, botId: Int): Boolean {
        val otp = mockDatabase.oneTimePasswords!!
        return otp[botId] == emailConfirmationParams.otp
    }

    override suspend fun unregister(botId: Int): Boolean {
        val bots = mockDatabase!!.bots!!
        return bots.removeIf { bot -> bot.id == botId }
    }
}