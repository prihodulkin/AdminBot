package com.admin_bot.plugins.mocks.model.login

import com.admin_bot.plugins.mocks.database.MockDatabase
import com.admin_bot.features.login.model.LoginRepository

class MockLoginRepository(private val mockDatabase: MockDatabase) : LoginRepository() {
    override suspend fun loginWithEmail(email: String, password: String): Int? {
        val botInfo = mockDatabase.bots!!.firstOrNull { bot -> bot.adminEmail == email }
            ?: return null
        return if (mockDatabase.botPasswords!![botInfo.id] == password) botInfo.id else null
    }

    override suspend fun loginWithAccessToken(accessToken: String, password: String): Int? {
        val botInfo = mockDatabase.bots!!.firstOrNull { bot -> bot.token == accessToken }
            ?: return null
        return if (mockDatabase.botPasswords!![botInfo.id] == password) botInfo.id else null
    }
}