package com.admin_bot.plugins.mocks.model.registration;

import com.admin_bot.features.registration.data.OtpData
import com.admin_bot.features.registration.model.OtpStorage
import com.admin_bot.plugins.mocks.database.MockDatabase

class MockOtpStorage(private val mockDatabase: MockDatabase): OtpStorage {
    override suspend fun saveOtpData(botId: Long, otpData: OtpData) {
        mockDatabase.oneTimePasswords!![botId] = otpData
    }

    override suspend fun getOtpData(botId: Long): OtpData? {
        return mockDatabase.oneTimePasswords!![botId]
    }

    override suspend fun deleteOtpData(botId: Long): Boolean {
        val result = mockDatabase.oneTimePasswords!!.remove(botId)
        return result==null
    }
}
