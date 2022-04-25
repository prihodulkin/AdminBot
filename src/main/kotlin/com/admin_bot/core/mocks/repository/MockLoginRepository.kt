package com.admin_bot.core.mocks.repository

import com.admin_bot.core.mocks.MockDatabase
import com.admin_bot.features.login.repository.LoginRepository

class MockLoginRepository(private val mockDatabase: MockDatabase) : LoginRepository() {
    override suspend fun loginWithEmail(email: String, password: String): Int? {
        val botInfo = mockDatabase.bots!!.firstOrNull { bot -> bot.adminEmail == email }
            ?: return null
        return if (mockDatabase.botPasswords!![botInfo.id] == password) botInfo.id else null
    }

    override suspend fun loginWithAccessToken(accessToken: String, password: String): Int? {
        val botInfo = mockDatabase.bots!!.firstOrNull { bot -> bot.accessToken == accessToken }
            ?: return null
        return if (mockDatabase.botPasswords!![botInfo.id] == password) botInfo.id else null
    }
}