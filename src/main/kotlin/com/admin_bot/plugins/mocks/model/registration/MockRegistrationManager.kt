package com.admin_bot.plugins.mocks.model.registration

import com.admin_bot.common.errors.IncorrectIdException
import com.admin_bot.features.bot_managing.data.BotInfo
import com.admin_bot.features.registration.data.RegisterParams
import com.admin_bot.features.registration.model.RegistrationManager
import com.admin_bot.plugins.mocks.database.MockDatabase

class MockRegistrationManager(private val mockDatabase: MockDatabase) : RegistrationManager {
    override suspend fun register(registerParams: RegisterParams): Long? {
        val bots = mockDatabase.bots!!
        val botInfo = bots.firstOrNull { bot -> bot.token == registerParams.token }
        if (botInfo != null) {
            return null
        }
        val id = bots.size.toLong()
        bots.add(BotInfo(id = id, token = registerParams.token))
        return id
    }

    override suspend fun isEmailNotBusy(email: String): Boolean {
        val bots = mockDatabase.bots!!
        val botInfo = bots.firstOrNull { bot -> bot.adminEmail == email }
        return botInfo == null
    }

    override suspend fun completeRegistration(botId: Long, email: String) {
        val bots = mockDatabase.bots!!
        val index = bots.indexOfFirst { bot -> bot.id == botId }
        if (index == -1) {
            throw IncorrectIdException()
        }
        bots[index] = bots[index].copy(adminEmail = email)
    }

    override suspend fun unregister(botId: Long): Boolean {
        val bots = mockDatabase!!.bots!!
        return bots.removeIf { bot -> bot.id == botId }
    }
}