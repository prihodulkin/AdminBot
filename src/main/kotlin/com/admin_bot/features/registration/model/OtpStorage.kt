package com.admin_bot.features.registration.model

import com.admin_bot.features.registration.data.OtpData

interface OtpStorage {
    /**
     * Save [otpData] for [botId]. Rewrite if it only exists for this [botId]
     */
    suspend fun saveOtpData(botId: Long, otpData: OtpData)

    /**
     * Returns [OtpData] saved for this [botId] or null if it has no one
     */
    suspend fun getOtpData(botId: Long):OtpData?

    /**
     * Deletes [OtpData] saved for this [botId]. Returns true if bot with [botId] was deleted successfully
     */
    suspend fun deleteOtpData(botId: Long):Boolean
}